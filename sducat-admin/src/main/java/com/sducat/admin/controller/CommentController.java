package com.sducat.admin.controller;

import com.sducat.common.annotation.validation.EnumValidation;
import com.sducat.common.constant.Constants;
import com.sducat.common.core.result.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CommentError;
import com.sducat.common.enums.CommentCheckTypeEnum;
import com.sducat.common.enums.CommentListTypeEnum;
import com.sducat.common.enums.CommentStatusEnum;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.framework.service.TokenService;
import com.sducat.system.data.dto.CommentDto;
import com.sducat.system.data.vo.CommentVo;
import com.sducat.system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by skyyemperor on 2021-02-01 23:32
 * Description :
 */
@RequestMapping("/comment")
@RestController
@Validated
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    @GetMapping("/info/{commentId}")
    public Result getCommentInfo(@PathVariable Long commentId) {
        CommentVo commentVo = commentService.getCommentInfo(tokenService.getUserId(), commentId);
        if (commentVo == null) return Result.getResult(CommentError.COMMENT_NOT_EXIST);
        return Result.success(CommentDto.getDto(commentVo));
    }

    /**
     * 获取猫谱下的评论
     */
    @GetMapping("/spectrum/list")
    public Result getSpectrumComments(
            @RequestParam Long catId,
            @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
            @Min(0) @RequestParam(defaultValue = "10") Integer pageSize,
            @EnumValidation(clazz = CommentListTypeEnum.class, message = "排序类型只能为：1(发布时间)，2(点赞量)")
            @RequestParam(defaultValue = "1") Integer type) {
        return commentService.getCommentList(tokenService.getUserId(), catId, CommentStatusEnum.SPECTRUM.getKey(),
                pageNum, pageSize, CommentListTypeEnum.getEnum(type));
    }

    /**
     * 获取社区下的评论
     */
    @GetMapping("/community/list")
    public Result getCommunityComments(
            @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
            @Min(0) @RequestParam(defaultValue = "10") Integer pageSize,
            @EnumValidation(clazz = CommentListTypeEnum.class, message = "排序类型只能为：1(发布时间)，2(点赞量)")
            @RequestParam(defaultValue = "1") Integer type) {
        return commentService.getCommentList(tokenService.getUserId(), null, CommentStatusEnum.COMMUNITY.getKey(),
                pageNum, pageSize, CommentListTypeEnum.getEnum(type));
    }

    /**
     * 获取自己发表的评论
     */
    @PreAuthorize("@pms.hasPerm('comment:personal:*')")
    @GetMapping("/personal/submitted")
    Result getPersonalCommentList(@RequestParam("_uid_") Long userId,
                                  @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                                  @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(commentService.getPersonalCommentList(userId, pageNum, pageSize));
    }

    /**
     * 获取自己赞过的评论
     */
    @PreAuthorize("@pms.hasPerm('comment:personal:*')")
    @GetMapping("/personal/liked")
    Result getLikedCommentList(@RequestParam("_uid_") Long userId,
                               @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                               @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(commentService.getLikedCommentList(userId, pageNum, pageSize));
    }

    /**
     * 在猫谱界面下发表评论
     *
     * @param userId        用户ID
     * @param catId         猫咪ID
     * @param content       评论内容
     * @param pic           评论图片
     * @param syncCommunity 是否同步到社区
     * @return
     */
    @PreAuthorize("@pms.hasPerm('comment:spectrum:add')")
    @PostMapping("/spectrum/add")
    public Result addCommentAfterSpectrum(@RequestParam("_uid_") Long userId,
                                          @RequestParam Long catId,
                                          @Size(max = 140, message = "评论字数不能超过140字哦") @RequestParam String content,
                                          @RequestParam(required = false) String[] pic,
                                          @RequestParam(defaultValue = "false") boolean syncCommunity) {
        if (pic != null && pic.length > Constants.PIC_MAX_NUM)
            return Result.getResult(CommonError.PIC_NUM_EXCEED_MAX);
        if (!qiNiuYunPicUtil.checkPicDomain(pic))
            return Result.getResult(CommonError.PIC_DOMAIN_WRONG);
        if (content.length() == 0 && (pic == null || pic.length == 0))
            return Result.getResult(CommentError.PARAM_CAN_NOT_EMPTY);

        return commentService.addSpectrumComment(userId, catId, content, pic, syncCommunity);
    }

    /**
     * 在社区界面下发表评论
     *
     * @param userId  用户ID
     * @param content 评论内容
     * @param pic     评论的图片
     * @return
     */
    @PreAuthorize("@pms.hasPerm('comment:community:add')")
    @PostMapping("/community/add")
    public Result addCommentInCommunity(@RequestParam("_uid_") Long userId,
                                        @Size(max = 140, message = "评论字数不能超过140字哦") @RequestParam String content,
                                        @RequestParam(required = false) String[] pic) {
        if (pic != null && pic.length > Constants.PIC_MAX_NUM)
            return Result.getResult(CommonError.PIC_NUM_EXCEED_MAX);
        if (!qiNiuYunPicUtil.checkPicDomain(pic))
            return Result.getResult(CommonError.PIC_DOMAIN_WRONG);
        if (content.length() == 0 && (pic == null || pic.length == 0))
            return Result.getResult(CommentError.PARAM_CAN_NOT_EMPTY);

        return commentService.addCommunityComment(userId, content, pic);
    }


    /**
     * 删除自己发布的评论
     */
    @PreAuthorize("@pms.hasPerm('comment:own:delete')")
    @PostMapping("/own/delete/{commentId}")
    public Result deleteOwnComment(@RequestParam("_uid_") Long userId,
                                   @PathVariable Long commentId) {
        return commentService.deleteOwnComment(userId, commentId);
    }

    /**
     * 删除违禁评论(admin)
     */
    @PreAuthorize("@pms.hasPerm('comment:forbidden:delete')")
    @PostMapping("/forbidden/delete/{commentId}")
    public Result deleteForbiddenComment(@PathVariable Long commentId) {
        return commentService.deleteForbiddenComment(commentId);
    }

    /**
     * 点赞或取消点赞。若未点过赞，则点赞；若已点过赞，则为取消点赞。
     */
    @PreAuthorize("@pms.hasPerm('comment:like')")
    @PostMapping("/like/{commentId}")
    public Result likeOrUnlikeComment(@RequestParam("_uid_") Long userId,
                                      @PathVariable Long commentId) {
        return commentService.likeOrUnlikeComment(userId, commentId);
    }

    /**
     * 获取未审核的评论
     */
    @PreAuthorize("@pms.hasPerm('comment:check')")
    @GetMapping("/no_check/list")
    Result getNoCheckComment(@Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                             @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(commentService.getNoCheckComment(pageNum, pageSize));
    }

    /**
     * 判断评论是否通过
     *
     * @param commentId 评论ID
     * @param type      评论审核的类型
     * @param catId     若属于评论中提及老猫类型，则需要带上catId参数，表示要跳转的猫咪
     */
    @PreAuthorize("@pms.hasPerm('comment:check')")
    @PostMapping("/check")
    Result checkComment(@RequestParam Long commentId,
                        @EnumValidation(clazz = CommentCheckTypeEnum.class) @RequestParam Integer type,
                        @RequestParam(required = false) Long catId) {
        return commentService.checkComment(commentId, CommentCheckTypeEnum.getEnum(type), catId);
    }

}
