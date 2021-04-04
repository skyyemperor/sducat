package com.sducat.system.service;

import com.sducat.system.data.po.QuickReport;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface QuickReportService extends IService<QuickReport> {

    /**
     * 更新速报
     */
    void updateQuickReport(List<QuickReport> quickReportList);

    /**
     * 获取速报
     */
    List<QuickReport> getQuickReportList();

}

