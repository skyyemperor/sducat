package com.sducat.common.core.result.error;

import com.sducat.common.core.result.ResultError;

public enum ScienceError implements ResultError {
    SCIENCE_MODULE_NOT_EXIST(40401, "这个科普板块不存在哦"),

    ;

    private int code;

    private String message;


    private ScienceError(int code, String message) {
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