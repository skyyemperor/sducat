package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-02-01 20:10
 * Description :
 */
@Getter
public enum PicUpdateTypeEnum {

    ROUND("round", "圆形图片"),
    MAIN("main", "猫咪主图"),
    ;

    private final String key;
    private final String remark;

    PicUpdateTypeEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(String key) {
        for (PicUpdateTypeEnum enums : PicUpdateTypeEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

    public static PicUpdateTypeEnum getEnumByKey(String key) {
        for (PicUpdateTypeEnum enums : PicUpdateTypeEnum.values()) {
            if (enums.key.equals(key)) return enums;
        }
        return PicUpdateTypeEnum.MAIN;
    }

}