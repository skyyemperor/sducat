package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-02-06 22:19
 * Description : 获取评论列表接口的排序类型
 */
@Getter
public enum CommentListTypeEnum {
    DATE(1, "date"), //发布时间
    LIKE_COUNT(2, "like"), //点赞量
    ;

    private final Integer key;
    private final String remark;

    CommentListTypeEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (CommentListTypeEnum enums : CommentListTypeEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

    public static CommentListTypeEnum getEnum(Integer key) {
        for (CommentListTypeEnum enums : CommentListTypeEnum.values()) {
            if (enums.key.equals(key)) return enums;
        }
        return null;
    }
}
