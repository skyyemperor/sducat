package com.sducat.common.core.result.error;

import com.sducat.common.core.result.ResultError;

/**
 * Created by skyyemperor on 2021-02-02 23:27
 * Description :
 */
public enum CommentError implements ResultError {
    COMMENT_NOT_EXIST(40301, "肿么肥事，这条评论走丢啦"),
    PARAM_CAN_NOT_EMPTY(40302, "评论内容不能为空"),
    DEL_OTHER_COMMENT_WRONG(40302, "无权删除他人的评论"),
    COMMENT_CHECK_TYPE_MUST_BE_COMMUNITY(40303, "该类型的评论审核要求评论为社区类型"),

    ;

    private int code;

    private String message;


    private CommentError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}