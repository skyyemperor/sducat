package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-02-06 21:15
 * Description :
 */
@Getter
public enum CommentStatusEnum {

    VIOLATION(0, "违规"),
    SPECTRUM(1, "猫谱"),
    COMMUNITY(2, "社区"),
    SPECTRUM_AND_COMMUNITY(3, "猫谱和社区"),

    ;

    private final Integer key;
    private final String remark;

    CommentStatusEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (CommentStatusEnum enums : CommentStatusEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }
}