package com.sducat.admin.controller;

import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.QiNiuYunPicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by skyyemperor on 2021-02-19 15:55
 * Description :
 */
@RequestMapping("/common")
@RestController
@Validated
public class CommonController {

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    //允许的后缀
    private static final HashSet<String> ALLOW_SUFFIX = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png"
    ));

    @PreAuthorize("@pms.hasPerm('common:pic:submit')")
    @PostMapping("/pic/submit")
    public Result submitPic(@RequestParam MultipartFile pic) {
        if (!checkPicSuffix(pic))
            return Result.getResult(CommonError.PIC_FORMAT_ERROR);
        return Result.success(qiNiuYunPicUtil.uploadByMultipartFile(pic));
    }

    private boolean checkPicSuffix(MultipartFile... files) {
        for (MultipartFile file : files) {
            String picName = file.getOriginalFilename();
            int dotIndex = picName.lastIndexOf(".");
            if (dotIndex == -1) //若文件名不存在 "."
                return false;
            if (!ALLOW_SUFFIX.contains(picName.substring(dotIndex)))
                return false;
        }
        return true;
    }
}