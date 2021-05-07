package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-03-02
 * Description : 评论审核未通过
 */
public class CommentCheckSuccessNotice extends AbstractNotice {
    public CommentCheckSuccessNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.CHECK_NOTICE;
    }

    @Override
    protected String title() {
        return "评论已通过审核";
    }

    @Override
    protected String content(Object... params) {
        //%tF %1$tR 评论发表时间
        return String.format("亲爱的猫友，您于%tF %1$tR发表的评论已通过审核，可以前去查看啦", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.COMMENT;
    }
}