package com.sducat.system.service.impl;

import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CatError;
import com.sducat.common.core.result.error.CommentError;
import com.sducat.common.enums.CommentCheckTypeEnum;
import com.sducat.common.enums.CommentListTypeEnum;
import com.sducat.common.enums.CommentStatusEnum;
import com.sducat.common.util.JSONUtil;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.common.util.TaskExecutorUtil;
import com.sducat.system.data.dto.CommentDto;
import com.sducat.system.data.po.notice.CommentBeLikedNotice;
import com.sducat.system.data.po.notice.CommentCheckFailNotice;
import com.sducat.system.data.po.notice.CommentMentionNewCatNotice;
import com.sducat.system.data.po.notice.CommentMentionOldCatNotice;
import com.sducat.system.data.vo.CommentVo;
import com.sducat.system.mapper.CommentLikeLinkMapper;
import com.sducat.system.service.CatService;
import com.sducat.system.service.NoticeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.data.po.Comment;
import com.sducat.system.mapper.CommentMapper;
import com.sducat.system.service.CommentService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by skyyemperor on 2021-02-01 23:04
 * Description :
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentLikeLinkMapper likeLinkMapper;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CatService catService;

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    @Override
    public CommentVo getCommentInfo(Long userId, Long commentId) {
        return commentMapper.selectCommentByCommentId(userId, commentId);
    }

    @Override
    public Result getCommentList(Long userId, Long catId, Integer status, Integer pageNum, Integer pageSize, CommentListTypeEnum type) {
        Integer commentNum = commentMapper.countCommentList(catId, status);
        Integer totalPageNum = (commentNum - 1) / pageSize + 1;

        if (pageNum != 1 && pageNum > totalPageNum) return Result.getResult(CommonError.THIS_IS_LAST_PAGE);
        if (pageNum <= 0) return Result.getResult(CommonError.THIS_IS_FIRST_PAGE);

        //从数据库中获取评论列表并将其类型转化为CommentDto
        List<CommentVo> commentList = commentMapper.selectCommentList(
                userId, catId, status, (pageNum - 1) * pageSize, pageSize, type);
        List<CommentDto> commentDtoList = commentList.stream().map(CommentDto::getDto).collect(Collectors.toList());

        return Result.success(MapBuildUtil.builder()
                .data("totalPageNum", totalPageNum)
                .data("commentNum", commentNum)
                .data("pageNum", pageNum)
                .data("pageSize", pageSize)
                .data("comments", commentDtoList)
                .get());
    }

    @Override
    public List<CommentDto> getPersonalCommentList(Long userId, Integer pageNum, Integer pageSize) {
        List<CommentVo> commentList = commentMapper.selectPersonalCommentList(
                userId, (pageNum - 1) * pageSize, pageSize);
        return commentList.stream().map(CommentDto::getDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getLikedCommentList(Long userId, Integer pageNum, Integer pageSize) {
        List<CommentVo> commentList = commentMapper.selectLikedCommentList(
                userId, (pageNum - 1) * pageSize, pageSize);
        return commentList.stream().map(CommentDto::getDto).collect(Collectors.toList());
    }

    @Override
    public Result addSpectrumComment(Long userId, Long catId, String content,
                                     String[] pic, boolean syncCommunity) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setCatId(catId);
        comment.setContent(content);
        comment.setPic(JSONUtil.toJson(pic));
        comment.setDate(new Date());
        //插入一条新的评论时使用对应的负数
        comment.setStatus(syncCommunity
                ? -CommentStatusEnum.SPECTRUM_AND_COMMUNITY.getKey()
                : -CommentStatusEnum.SPECTRUM.getKey());

        this.save(comment);
        return Result.success();
    }

    @Override
    public Result addCommunityComment(Long userId, String content, String[] pic) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setPic(JSONUtil.toJson(pic));
        comment.setDate(new Date());
        //插入一条新的评论时使用对应的负数
        comment.setStatus(-CommentStatusEnum.COMMUNITY.getKey());

        this.save(comment);
        return Result.success();
    }

    @Override
    public Result deleteOwnComment(Long userId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) return Result.getResult(CommentError.COMMENT_NOT_EXIST);
        if (!userId.equals(comment.getUserId()))
            return Result.getResult(CommentError.DEL_OTHER_COMMENT_WRONG);

        commentMapper.deleteById(commentId);

        return Result.success();
    }

    @Override
    public Result deleteForbiddenComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) return Result.getResult(CommentError.COMMENT_NOT_EXIST);

        commentMapper.deleteById(commentId);
        noticeService.addNotice(new CommentCheckFailNotice(
                comment.getUserId(), null, comment.getDate()
        ));
        return Result.success();
    }

    @Override
    @Transactional
    public Result likeOrUnlikeComment(Long userId, Long commentId) {
        //判断评论是否存在
        CommentVo comment = getCommentInfo(userId, commentId);
        if (comment == null)
            return Result.getResult(CommentError.COMMENT_NOT_EXIST);

        if (judgeCommentLike(userId, commentId)) {
            //已经点过赞，下面为取消点赞
            likeLinkMapper.unLikeComment(userId, commentId);
            commentMapper.updateCommentLike(commentId, -1);
            comment.setLike(comment.getLike() - 1);
        } else {
            //没有点过赞
            likeLinkMapper.likeComment(userId, commentId);
            commentMapper.updateCommentLike(commentId, 1);
            comment.setLike(comment.getLike() + 1);
            //发送点赞通知
            noticeService.addNotice(new CommentBeLikedNotice(
                    comment.getUser().getUserId(), commentId.toString(), userId, commentId
            ));
        }
        return Result.success(MapBuildUtil.builder()
                .data("commentId", commentId)
                .data("like", comment.getLike())
                .data("liked", !comment.getLiked())
                .get());
    }

    @Override
    public boolean judgeCommentLike(Long userId, Long commentId) {
        return likeLinkMapper.judge(userId, commentId) != null;
    }

    @Override
    public List<CommentDto> getNoCheckComment(Integer pageNum, Integer pageSize) {
        List<CommentVo> commentVoList = commentMapper.selectNoCheckComment(null, (pageNum - 1) * pageSize, pageSize);
        return commentVoList.stream().map(CommentDto::getDto).collect(Collectors.toList());
    }

    @Override
    public Result checkComment(Long commentId, CommentCheckTypeEnum type, Long catId) {
        Comment comment = commentMapper.selectById(commentId);
        //判断评论是否存在
        if (comment == null)
            return Result.getResult(CommentError.COMMENT_NOT_EXIST);
        //判断评论是否已经审核过
        if (comment.getStatus() >= 0)
            return Result.getResult(CommonError.HAD_CHECKED);

        switch (type) {
            case CHECK_FAIL:
                comment.setStatus(type.getKey()); //审核未通过的状态 0
                noticeService.addNotice(new CommentCheckFailNotice(
                        comment.getUserId(), null, comment.getDate()
                ));
                break;
            case CHECK_SUCCESS:
                comment.setStatus(Math.abs(comment.getStatus())); //审核通过，状态设为正数
                break;
            case MENTION_OLD_CAT:
                if (catId == null || catService.getCatInfo(catId) == null)
                    return Result.getResult(CatError.CAT_IS_NOT_EXIST);
                //判断评论是否为社区类型
                if (Math.abs(comment.getStatus()) != CommentStatusEnum.COMMUNITY.getKey())
                    return Result.getResult(CommentError.COMMENT_CHECK_TYPE_MUST_BE_COMMUNITY);
                comment.setStatus(Math.abs(comment.getStatus()));
                noticeService.addNotice(new CommentMentionOldCatNotice(
                        comment.getUserId(), catId.toString(), comment.getDate()
                ));
                break;
            case MENTION_NEW_CAT:
                //判断评论是否为社区类型
                if (Math.abs(comment.getStatus()) != CommentStatusEnum.COMMUNITY.getKey())
                    return Result.getResult(CommentError.COMMENT_CHECK_TYPE_MUST_BE_COMMUNITY);
                comment.setStatus(Math.abs(comment.getStatus()));
                noticeService.addNotice(new CommentMentionNewCatNotice(
                        comment.getUserId(), null, comment.getDate()
                ));
                break;
        }

        commentMapper.updateById(comment); //更新数据库中comment的状态
        return Result.success();
    }


}


