package com.sducat.system.data.po.notice;

import com.sducat.common.constant.Constants;
import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

import java.util.Date;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
public abstract class AbstractNotice extends Notice {

    public AbstractNotice(Long userId, String skipLink, Long relatedUserId, Long relatedCommentId, Object... params) {
        super.setUserId(userId);
        super.setSkipLink(skipLink);
        super.setRelatedUserId(relatedUserId);
        super.setRelatedCommentId(relatedCommentId);
        super.setDate(new Date());
        super.setRead(Constants.NO);
        super.setNoticeType(noticeType().getKey());
        super.setTitle(title());
        super.setContent(content(params));
        super.setSkipType(skipType().getKey());
    }

    public AbstractNotice(Long userId, String skipLink, Object... params) {
        super.setUserId(userId);
        super.setSkipLink(skipLink);
        super.setDate(new Date());
        super.setRead(Constants.NO);
        super.setNoticeType(noticeType().getKey());
        super.setTitle(title());
        super.setContent(content(params));
        super.setSkipType(skipType().getKey());
    }

    public AbstractNotice() {
    }

    protected abstract NoticeTypeEnum noticeType();

    protected abstract String title();

    protected abstract String content(Object... params);

    protected abstract SkipLinkTypeEnum skipType();

}
