package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 猫咪反馈已收到 通知
 */
public class CatFeedbackCheckFailNotice extends AbstractNotice {
    public CatFeedbackCheckFailNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink,  params);
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
        //%s catName  %s 审核不通过的原因
        return String.format("亲爱的猫友，感谢您为%s反馈的信息，但经过我们认真的考证，%s", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.CAT;
    }
}
