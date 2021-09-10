package com.sducat.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.sducat.common.core.result.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.OkHttpUtil;
import com.sducat.system.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by skyyemperor on 2021-08-15
 * Description :
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public Result uploadPic(MultipartFile file) {
        try {
            String res = OkHttpUtil.getInstance().postMultiFile(
                    "https://img.sducat.top/pic/upload",
                    MapBuildUtil.builder().data("file", file).get()
            );
            Result result = JSON.parseObject(res, Result.class);
            if (result.getCode() == 0) {
                return Result.success(((Map) result.getData()).get("mainUrl"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.getResult(CommonError.PIC_UPLOAD_WRONG);
    }
}
