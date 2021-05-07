package com.sducat.common.core.result;

import com.sducat.common.constant.Constants;
import com.sducat.common.core.result.ResultError;

public enum CommonError implements ResultError {
    LOGIN_STATUS_IS_INVALID(40001, "登录状态已失效"),
    NETWORK_WRONG(40002, "网络错误"),
    PERM_NOT_ALLOW(40003, "无权限访问"),
    METHOD_NOT_ALLOW(40005, "方法不允许"),
    PARAM_FORMAT_WRONG(40006, "参数范围或格式错误"),
    VERIFY_WRONG(40007, "参数校验失败"),
    REQUEST_TIMEOUT(40008, "请求时间校验失败"),
    THIS_IS_LAST_PAGE(40009, "再怎么找也没有啦"),
    THIS_IS_FIRST_PAGE(40010, "没有上一页啦"),
    PIC_UPLOAD_WRONG(40011, "图片上传失败"),
    PIC_FORMAT_ERROR(40012, "图片格式只能为jpg, jpeg, png"),
    PIC_DOMAIN_WRONG(40013,"图片来源错误"),
    PIC_NUM_EXCEED_MAX(40014, "图片最多只能上传" + Constants.PIC_MAX_NUM + "张"),
    HAD_CHECKED(40015, "已经审核过，无需再次审核"),
    ;


    private int code;

    private String message;


    private CommonError(int code, String message) {
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
