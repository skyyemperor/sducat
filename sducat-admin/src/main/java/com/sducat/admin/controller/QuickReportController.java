package com.sducat.admin.controller;

import com.sducat.common.core.result.Result;
import com.sducat.system.data.po.QuickReport;
import com.sducat.system.service.QuickReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/quick_report")
@RestController
@Validated
public class QuickReportController {

    @Autowired
    private QuickReportService reportService;

    /**
     * 更新速报
     */
    @PreAuthorize("@pms.hasPerm('quick_report:update')")
    @PostMapping("/update")
    Result updateQuickReport(@Valid @RequestBody List<QuickReport> quickReportList) {
        reportService.updateQuickReport(quickReportList);
        return Result.success();
    }

    /**
     * 获取速报
     */
    @GetMapping("/list")
    Result getQuickReportList() {
        return Result.success(reportService.getQuickReportList());
    }


}
