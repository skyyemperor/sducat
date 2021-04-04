package com.sducat.common.core.result.error;

import com.sducat.common.core.result.ResultError;

public enum NoticeError implements ResultError {
    NOTICE_NOT_EXIST(40501, "啊哦，这条通知走丢啦"),

    ;

    private int code;

    private String message;


    private NoticeError(int code, String message) {
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