package com.sducat.admin.controller;

import com.sducat.common.annotation.validation.EnumValidation;
import com.sducat.common.core.result.Result;
import com.sducat.common.enums.NoticeTypeEnum;
import com.sducat.system.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * Created by skyyemperor on 2021-03-02
 * Description : 通知模块controller
 */
@RequestMapping("/notice")
@RestController
@Validated
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 获取通知
     */
    @PreAuthorize("@pms.hasPerm('notice:personal:*')")
    @GetMapping("/list")
    public Result getNoticeList(@RequestParam("_uid_") Long userId,
                                @EnumValidation(clazz = NoticeTypeEnum.class) @RequestParam(defaultValue = "1") Integer type,
                                @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                                @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(noticeService.getNoticeList(userId, type, pageNum, pageSize));
    }

    /**
     * 设置通知为已读
     */
    @PreAuthorize("@pms.hasPerm('notice:personal:*')")
    @PostMapping("/read")
    public Result readNotice(@RequestParam("_uid_") Long userId,
                             @RequestParam Long noticeId) {
        return noticeService.readNotice(userId, noticeId);
    }
}
