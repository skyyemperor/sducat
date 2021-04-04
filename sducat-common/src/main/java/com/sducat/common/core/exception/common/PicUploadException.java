package com.sducat.common.core.exception.common;

import com.sducat.common.core.exception.BaseException;
import com.sducat.common.core.result.error.CommonError;

/**
 * Created by skyyemperor on 2021-02-18 14:45
 * Description :
 */
public class PicUploadException extends BaseException {
    public PicUploadException() {
        super(CommonError.PIC_UPLOAD_WRONG.getCode(),
                CommonError.PIC_UPLOAD_WRONG.getMessage());
    }
}
