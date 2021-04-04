package com.sducat.admin.controller;

import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.system.data.po.Donation;
import com.sducat.system.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by skyyemperor on 2021-03-09
 * Description :
 */
@RestController
@RequestMapping("/donation")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    /**
     * 添加捐款公示
     */
    @PreAuthorize("@pms.hasPerm('donation:*:modify')")
    @PostMapping("/add")
    public Result addDonation(@RequestParam String userName,
                              @RequestParam Integer money,
                              @RequestParam Integer year,
                              @RequestParam Integer month) {
        donationService.addDonation(userName, money, year, month);
        return Result.success();
    }

    /**
     * 按月获取捐款公示名单
     */
    @GetMapping("/list")
    public Result getDonationList(@RequestParam Integer year,
                                  @RequestParam Integer month) {
        return Result.success(donationService.getDonationList(year, month));
    }

    /**
     * 更新打赏二维码
     */
    @PreAuthorize("@pms.hasPerm('donation:*:modify')")
    @PostMapping("/qrcode/update")
    public Result updateQRCode(@RequestParam String pic){
        if (!qiNiuYunPicUtil.checkPicDomain(pic))
            return Result.getResult(CommonError.PIC_DOMAIN_WRONG);
        donationService.updateQRCode(pic);
        return Result.success();
    }

    /**
     * 获取打赏二维码
     */
    @GetMapping("/qrcode")
    public Result getQRCode(){
        return Result.success(donationService.getQRCode());
    }
}
