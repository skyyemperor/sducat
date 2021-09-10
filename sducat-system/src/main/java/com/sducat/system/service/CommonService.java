package com.sducat.system.service;

import com.sducat.common.core.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by skyyemperor on 2021-08-15
 * Description :
 */
public interface CommonService {

    Result uploadPic(MultipartFile pic);

}
