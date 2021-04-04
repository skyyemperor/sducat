package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
@Getter
public enum NoticeTypeEnum {

    INTERACTIVE_NOTICE(1, "互动通知"),
    CHECK_NOTICE(2, "系统通知"),
    ;

    private final Integer key;
    private final String remark;

    NoticeTypeEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (NoticeTypeEnum enums : NoticeTypeEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

}
