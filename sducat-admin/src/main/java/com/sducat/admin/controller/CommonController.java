package com.sducat.admin.controller;

import com.sducat.common.core.result.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.system.service.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private CommonService commonService;

    //允许的后缀
    private static final HashSet<String> ALLOW_SUFFIX = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png"
    ));

    @PreAuthorize("@pms.hasPerm('common:pic:submit')")
    @GetMapping("/pic/submit/token")
    public Result getQiNiuYunToken() {
        String[] tokenAndDomain = qiNiuYunPicUtil.getTokenAndDomain();
        return Result.success(MapBuildUtil.builder()
                .data("qiniuyun_token", tokenAndDomain[0])
                .data("domain", tokenAndDomain[1])
                .get());
    }

    @PreAuthorize("@pms.hasPerm('common:pic:submit')")
    @PostMapping("/pic/submit")
    public Result submitPic(@RequestParam MultipartFile file) {
        if (!checkPicSuffix(file))
            return Result.getResult(CommonError.PIC_FORMAT_ERROR);
        return commonService.uploadPic(file);
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
