package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-03-02
 * Description :
 */
public class CommentMentionNewCatNotice extends AbstractNotice {
    public CommentMentionNewCatNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.CHECK_NOTICE;
    }

    @Override
    protected String title() {
        return "评论提及猫咪";
    }

    @Override
    protected String content(Object... params) {
        //%tF %1$tR 评论发表时间
        return String.format("亲爱的猫友，您于%tF %1$tR发表的评论中提及了一只未登记的猫咪，快来为它填写资料吧~", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.NEW_CAT;
    }
}