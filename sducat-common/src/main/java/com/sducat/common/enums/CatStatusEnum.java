package com.sducat.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by skyyemperor on 2021-01-30 21:03
 * Description : 在校状态枚举类
 */
@Getter
public enum CatStatusEnum {

    AT_SCHOOL(0, "在校"),
    HAVE_ADOPTED(1, "已被领养"),
    STRAY(2, "离校流浪"),
    MEOW_STAR(3, "去世"),
    WAIT_ADOPTED(4, "待领养"),
    ;

    private final Integer key;
    private final String remark;

    CatStatusEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (CatStatusEnum enums : CatStatusEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

    public static CatStatusEnum getEnum(Integer key) {
        for (CatStatusEnum enums : CatStatusEnum.values()) {
            if (enums.key.equals(key)) return enums;
        }
        return null;
    }

    public static CatStatusEnum getEnum(String remark) {
        for (CatStatusEnum enums : CatStatusEnum.values()) {
            if (enums.remark.equals(remark)) return enums;
        }
        return null;
    }
}
