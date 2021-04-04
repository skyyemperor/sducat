package com.sducat.common.enums;

import lombok.Getter;

/**
 * 校区的枚举类
 */
@Getter
public enum CatColorEnum {
    ORANGE("橘黄", "橘黄"),
    WHITE("白色", "白色"),
    ORANGE_WHITE("橘白", "橘白"),
    CIVET_FLOWER("狸花", "狸花"),
    THREE_FLOWERS("三花", "三花"),
    HAWKSBILL("玳瑁", "玳瑁"),
    THREE_FLOWERS_AND_HAWKSBILL("三花和玳瑁", "三花和玳瑁"),
    OTHERS("其他", "其他"),
    ;

    private final String key;
    private final String remark;

    CatColorEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(String key) {
        for (CatColorEnum enums : CatColorEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

}
