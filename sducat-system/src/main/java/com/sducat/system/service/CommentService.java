package com.sducat.system.service;

import com.sducat.common.core.result.Result;
import com.sducat.common.enums.CommentCheckTypeEnum;
import com.sducat.common.enums.CommentListTypeEnum;
import com.sducat.system.data.dto.CommentDto;
import com.sducat.system.data.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sducat.system.data.vo.CommentVo;

import java.util.List;

/**
 * Created by skyyemperor on 2021-02-01 23:04
 * Description :
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取评论具体信息
     */
    CommentVo getCommentInfo(Long userId, Long commentId);

    /**
     * 获取评论列表
     */
    Result getCommentList(Long userId, Long catId, Integer status, Integer pageNum, Integer pageSize, CommentListTypeEnum type);

    /**
     * 获取自己发表的评论
     */
    List<CommentDto> getPersonalCommentList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取自己赞过的评论
     */
    List<CommentDto> getLikedCommentList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 在猫咪详情页下评论
     */
    Result addSpectrumComment(Long userId, Long catId, String content, String[] pic, boolean syncCommunity);

    /**
     * 在社区评论
     */
    Result addCommunityComment(Long userId, String content, String[] pic);

    /**
     * 删除个人的评论
     */
    Result deleteOwnComment(Long userId, Long commentId);

    /**
     * 删除违禁评论
     */
    Result deleteForbiddenComment(Long commentId);

    /**
     * 点赞或取消点赞
     */
    Result likeOrUnlikeComment(Long userId, Long commentId);

    /**
     * 判断是否已点赞
     */
    boolean judgeCommentLike(Long userId, Long commentId);


    /**
     * 获取未审核的评论
     */
    List<CommentDto> getNoCheckComment(Integer pageNum, Integer pageSize);

    /**
     * 判断评论是否通过
     */
    Result checkComment(Long commentId, CommentCheckTypeEnum type,Long catId);
}


