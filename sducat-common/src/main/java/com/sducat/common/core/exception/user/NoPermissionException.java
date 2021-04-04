package com.sducat.common.core.exception.user;

import com.sducat.common.core.exception.BaseException;
import com.sducat.common.core.result.error.CommonError;

/**
 * Created by skyyemperor on 2021-02-03 22:58
 * Description : 无权限的异常类
 */
public class NoPermissionException extends BaseException {
    public NoPermissionException() {
        super(CommonError.PERM_NOT_ALLOW.getCode(),
                CommonError.PERM_NOT_ALLOW.getMessage());
    }
}
