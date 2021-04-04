package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 评论被点赞通知
 */
public class CommentBeLikedNotice extends AbstractNotice {
    public CommentBeLikedNotice(Long userId, String skipLink, Long relatedUserId, Long relatedCommentId, Object... params) {
        super(userId, skipLink, relatedUserId,relatedCommentId, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.INTERACTIVE_NOTICE;
    }

    @Override
    protected String title() {
        return "评论被点赞";
    }

    @Override
    protected String content(Object... params) {
        return "";
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.COMMENT;
    }
}
