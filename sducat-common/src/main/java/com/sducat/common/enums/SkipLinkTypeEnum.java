package com.sducat.common.enums;

import lombok.Getter;

/**
 * 跳转链接类型枚举类
 */
@Getter
public enum SkipLinkTypeEnum {
    URL("URL", "外部链接"),
    CAT("cat", "猫咪详情页"),
    COMMENT("comment", "评论"),
    NEW_CAT("new_cat", "报送新猫页面"),
    ADOPT_CAT("adopt_cat", "领养猫咪页面"),
    NO_SKIP(null, null),
    ;

    private final String key;
    private final String remark;

    SkipLinkTypeEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

}
