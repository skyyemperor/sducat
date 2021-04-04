package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 猫咪反馈已收到 通知
 */
public class CatFeedbackCheckSuccessNotice extends AbstractNotice {
    public CatFeedbackCheckSuccessNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.CHECK_NOTICE;
    }

    @Override
    protected String title() {
        return "审核结束";
    }

    @Override
    protected String content(Object... params) {
        //%s catName
        return String.format("亲爱的猫友，您为%s反馈的信息通过审核啦！信息现已更新，请点击查看", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.CAT;
    }
}
