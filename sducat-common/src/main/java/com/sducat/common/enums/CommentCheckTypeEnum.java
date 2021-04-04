package com.sducat.common.enums;

import lombok.Getter;

/**
 * Created by skyyemperor on 2021-03-02
 * Description : 评论审核类型
 */
@Getter
public enum CommentCheckTypeEnum {
    CHECK_FAIL(0, "审核未通过"),
    CHECK_SUCCESS(1, "审核通过"),
    MENTION_OLD_CAT(2, "评论提及老猫(社区中的评论)"),
    MENTION_NEW_CAT(3, "评论提及新猫(社区中的评论)"),
    ;

    private final Integer key;
    private final String remark;

    CommentCheckTypeEnum(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    public static String getRemark(Integer key) {
        for (CommentCheckTypeEnum enums : CommentCheckTypeEnum.values()) {
            if (enums.key.equals(key)) return enums.getRemark();
        }
        return null;
    }

    public static CommentCheckTypeEnum getEnum(Integer key) {
        for (CommentCheckTypeEnum enums : CommentCheckTypeEnum.values()) {
            if (enums.key.equals(key)) return enums;
        }
        return null;
    }

}