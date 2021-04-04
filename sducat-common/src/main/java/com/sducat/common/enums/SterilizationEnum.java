package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-01-30 21:50
 * Description : 绝育状态枚举类
 */
@Getter
public enum SterilizationEnum {

    NOT(0, "未绝育"),
    HAVE(1, "已绝育"),
    ;

    private final Integer key;
    private final String remark;

    SterilizationEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (SterilizationEnum enums : SterilizationEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }
}
