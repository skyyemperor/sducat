package com.sducat.system.service.impl;

import com.sducat.common.constant.Constants;
import com.sducat.common.core.redis.RedisCache;
import com.sducat.common.core.result.CommonError;
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
import com.sducat.system.data.po.notice.*;
import com.sducat.system.data.vo.CommentVo;
import com.sducat.system.mapper.CommentLikeLinkMapper;
import com.sducat.system.service.CatService;
import com.sducat.system.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private RedisCache redisCache;

    private static final String LIKE_OR_UNLIKE_LUA = "if redis.call('sismember', KEYS[1], KEYS[2]) == 1 " +
            "then redis.call('srem', KEYS[1], KEYS[2]) return 0 " +
            "else redis.call('sadd', KEYS[1], KEYS[2]) return 1 end";

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

        //?????????????????????????????????????????????????????????CommentDto
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
        //????????????????????????????????????????????????
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
        //????????????????????????????????????????????????
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
    public Result likeOrUnlikeComment(Long userId, Long commentId) {
        // ????????????????????????
        CommentVo comment = getCommentInfo(userId, commentId);
        if (comment == null)
            return Result.getResult(CommentError.COMMENT_NOT_EXIST);

        // ???redis???????????????????????????????????????????????????????????????1?????????????????????????????????0
        String commentSetRedisKey = Constants.COMMENT_SET_REDIS_KEY + commentId;
        Long liked = redisCache.executeLUA(LIKE_OR_UNLIKE_LUA, commentSetRedisKey, userId.toString());

        // ????????????mysql???????????????
        asyncUpdateMysqlLikeInfo(userId, comment, liked);

        return Result.success(MapBuildUtil.builder()
                .data("commentId", commentId)
                .data("liked", liked)
                .get());
    }

    /**
     * ????????????mysql?????????????????????????????????
     */
    private void asyncUpdateMysqlLikeInfo(Long userId, CommentVo comment, Long liked) {
        taskExecutorUtil.run(() -> {
            // ?????????????????????????????????mysql
            String commentSetRedisKey = Constants.COMMENT_SET_REDIS_KEY + comment.getCommentId();
            Long likeNum = redisCache.getSetSize(commentSetRedisKey);
            commentMapper.updateById(new Comment(comment.getCommentId(), likeNum.intValue()));

            if (liked == 1) { //????????????????????????mysql?????????????????????
                likeLinkMapper.likeComment(userId, comment.getCommentId());
                //??????????????????
                noticeService.addNotice(new CommentBeLikedNotice(
                        comment.getUser().getUserId(), comment.getCommentId().toString(), userId, comment.getCommentId()
                ));
            } else if (liked == 0) { //???????????????????????????mysql
                likeLinkMapper.unLikeComment(userId, comment.getCommentId());
            }
        });
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
        //????????????????????????
        if (comment == null)
            return Result.getResult(CommentError.COMMENT_NOT_EXIST);
        //?????????????????????????????????
        if (comment.getStatus() >= 0)
            return Result.getResult(CommonError.HAD_CHECKED);

        switch (type) {
            case CHECK_FAIL:
                comment.setStatus(type.getKey()); //???????????????????????? 0
                noticeService.addNotice(new CommentCheckFailNotice(
                        comment.getUserId(), null, comment.getDate()
                ));
                break;
            case CHECK_SUCCESS:
                comment.setStatus(Math.abs(comment.getStatus())); //?????????????????????????????????
                noticeService.addNotice(new CommentCheckSuccessNotice(
                        comment.getUserId(), commentId.toString(), comment.getDate()
                ));
                break;
            case MENTION_OLD_CAT:
                if (catId == null || catService.getCatInfo(catId) == null)
                    return Result.getResult(CatError.CAT_IS_NOT_EXIST);
                //?????????????????????????????????
                if (Math.abs(comment.getStatus()) != CommentStatusEnum.COMMUNITY.getKey())
                    return Result.getResult(CommentError.COMMENT_CHECK_TYPE_MUST_BE_COMMUNITY);
                comment.setStatus(Math.abs(comment.getStatus()));
                noticeService.addNotice(new CommentMentionOldCatNotice(
                        comment.getUserId(), catId.toString(), comment.getDate()
                ));
                break;
            case MENTION_NEW_CAT:
                //?????????????????????????????????
                if (Math.abs(comment.getStatus()) != CommentStatusEnum.COMMUNITY.getKey())
                    return Result.getResult(CommentError.COMMENT_CHECK_TYPE_MUST_BE_COMMUNITY);
                comment.setStatus(Math.abs(comment.getStatus()));
                noticeService.addNotice(new CommentMentionNewCatNotice(
                        comment.getUserId(), null, comment.getDate()
                ));
                break;
        }

        commentMapper.updateById(comment); //??????????????????comment?????????
        return Result.success();
    }


}


