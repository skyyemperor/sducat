package com.sducat.common.core.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * web层统一返回类型
 */
public class Result implements Serializable {
    private int code = 0;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Result() {
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ResultError resultError) {
        this.code = resultError.getCode();
        this.message = resultError.getMessage();
    }

    public static Result getResult(int code, String message) {
        return getResult(code, message, null);
    }

    public static Result getResult(int code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        return success("success", data);
    }

    public static Result success(String message, Object data) {
        return new Result(0, message, data);
    }

    public static Result fail() {
        return fail(null);
    }

    public static Result fail(Object data) {
        return fail("啊哦，请求出问题了〒▽〒", data);
    }

    public static Result fail(String message, Object data) {
        return new Result(-1, message, data);
    }

    public static Result getResult(ResultError resultError) {
        return getResult(resultError.getCode(), resultError.getMessage());
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Info{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
