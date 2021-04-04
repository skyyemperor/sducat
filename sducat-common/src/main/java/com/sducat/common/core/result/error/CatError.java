package com.sducat.common.core.result.error;

import com.sducat.common.core.result.ResultError;

/**
 * Created by skyyemperor on 2021-01-30 17:24
 * Description :
 */
public enum CatError implements ResultError {
    CAT_IS_NOT_EXIST(40201, "诶，这只猫咪走丢了"),
    FEEDBACK_NOT_EXIST(40203, "该反馈不存在"),
    NEW_CAT_NOT_EXIST(40204, "该新猫表单不存在"),


    ;

    private int code;

    private String message;


    private CatError(int code, String message) {
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