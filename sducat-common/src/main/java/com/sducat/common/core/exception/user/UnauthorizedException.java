package com.sducat.common.core.exception.user;

import com.sducat.common.core.exception.BaseException;
import com.sducat.common.core.result.error.CommonError;

/**
 * Created by skyyemperor on 2021-02-03 22:55
 * Description : 认证失败的异常类
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super(CommonError.LOGIN_STATUS_IS_INVALID.getCode(),
                CommonError.LOGIN_STATUS_IS_INVALID.getMessage());
    }
}
