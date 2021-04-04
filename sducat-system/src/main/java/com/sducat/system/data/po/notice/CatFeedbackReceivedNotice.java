package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 猫咪反馈已收到 通知
 */
public class CatFeedbackReceivedNotice extends AbstractNotice {
    public CatFeedbackReceivedNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.CHECK_NOTICE;
    }

    @Override
    protected String title() {
        return "收到反馈";
    }

    @Override
    protected String content(Object... params) {
        //%s catName
        return String.format("亲爱的猫友，我们已收到您为%s反馈的信息，信息正在核查中，我们会第一时间通知您结果", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.CAT;
    }
}
