package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-01-30 21:36
 * Description : 性别枚举类
 */
@Getter
public enum  SexEnum {

    MALE("公", "男猫"),
    FEMALE("母","女猫"),
    NOT_SURE("不明","不明"),
    ;

    private final String key;
    private final String remark;

    SexEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(String key) {
        for (SexEnum enums : SexEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

}
