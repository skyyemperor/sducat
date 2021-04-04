package com.sducat.common.enums;

import lombok.Getter;

/**
 * 校区的枚举类
 */
@Getter
public enum CampusEnum {
    CENTER("中心校区", "中心校区"),
    HONG_JIA_LOU("洪家楼校区", "洪家楼校区"),
    XING_LONG_SHAN("兴隆山校区", "兴隆山校区"),
    SOFTWARE("软件园校区", "软件园校区"),
    BAO_TU_SPRING("趵突泉校区", "趵突泉校区"),
    QIAN_FO_SHAN("千佛山校区", "千佛山校区"),
    QING_DAO("青岛校区", "青岛校区"),
    WEI_HAI("威海校区", "威海校区"),
    ;

    private final String key;
    private final String remark;

    CampusEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(String key) {
        for (CampusEnum enums : CampusEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

}
