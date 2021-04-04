package com.sducat.common.core.exception;


import com.sducat.common.core.result.ResultError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private Object data;


    public BaseException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseException(int code, String message) {
        this(code, message, null);
    }

    public BaseException(ResultError error) {
        this(error.getCode(), error.getMessage(), null);
    }
}