package com.sducat.common.core.result.error;

import com.sducat.common.core.result.ResultError;

/**
 * Created by skyyemperor on 2020-12-18 2:05
 * Description :
 */
public enum AuthError implements ResultError {
    LOGIN_FAIL(40101, "登录失败"),
    REFRESH_TOKEN_IS_NOT_VALID(40102,"refresh_token已失效"),
    USER_NOT_EXIST(40103,"该用户不存在"),
    NICKNAME_LENGTH_WRONG(40104, "用户名长度应为2~16位"),
    NICKNAME_CHARACTER_WRONG(40105, "用户名不可包含除-和_以外的特殊字符"),
    PASSWORD_LENGTH_WRONG(40106, "密码长度应为6~16位"),
    USERNAME_HAS_EXIST(40107, "用户名已存在"),

    ;

    private int code;

    private String message;


    private AuthError(int code, String message) {
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
