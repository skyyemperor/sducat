package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 新猫审核未通过
 */
public class NewCatCheckSuccessNotice extends AbstractNotice {

    public NewCatCheckSuccessNotice(Long userId, String skipLink, Object... params) {
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
        return String.format("亲爱的猫友，您于%tF上报的猫咪确实是新成员，感谢您的发现，猫咪现已入库，请点击查看", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.CAT;
    }
}
