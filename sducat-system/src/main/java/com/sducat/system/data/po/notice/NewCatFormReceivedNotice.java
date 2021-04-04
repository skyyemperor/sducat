package com.sducat.system.data.po.notice;

import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.common.enums.SkipLinkTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * Created by skyyemperor on 2021-02-25
 * Description : 上报新猫收到表单
 */
public class NewCatFormReceivedNotice extends AbstractNotice {
    public NewCatFormReceivedNotice(Long userId, String skipLink, Object... params) {
        super(userId, skipLink, params);
    }

    @Override
    protected NoticeTypeEnum noticeType() {
        return NoticeTypeEnum.CHECK_NOTICE;
    }

    @Override
    protected String title() {
        return "收到表单";
    }

    @Override
    protected String content(Object... params) {
        return String.format("亲爱的猫友，我们已收到您上报的新猫咪信息，信息正在核查中，我们会第一时间通知您结果", params);
    }

    @Override
    protected SkipLinkTypeEnum skipType() {
        return SkipLinkTypeEnum.NO_SKIP;
    }

}
