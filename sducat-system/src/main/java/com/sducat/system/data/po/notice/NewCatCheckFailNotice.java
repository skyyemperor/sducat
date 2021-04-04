package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 新猫审核未通过
 */
public class NewCatCheckFailNotice extends AbstractNotice {

    public NewCatCheckFailNotice(Long userId, String skipLink, Object... params) {
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
        return String.format("亲爱的猫友，您于%tF上报的猫咪并不是新猫哦", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.NO_SKIP;
    }
}
