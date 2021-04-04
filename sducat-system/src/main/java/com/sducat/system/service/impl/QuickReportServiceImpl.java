package com.sducat.system.service.impl;

import com.sducat.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.data.po.QuickReport;
import com.sducat.system.mapper.QuickReportMapper;
import com.sducat.system.service.QuickReportService;

@Service
public class QuickReportServiceImpl extends ServiceImpl<QuickReportMapper, QuickReport> implements QuickReportService {

    @Autowired
    private QuickReportMapper quickReportMapper;

    @Override
    public void updateQuickReport(List<QuickReport> quickReportList) {
        //首先将原有速报status设为0过期
        quickReportMapper.setAllReportsInvalid();

        for (QuickReport report : quickReportList) {
            report.setStatus(Constants.YES);
            report.setDate(new Date());
            quickReportMapper.insert(report);
        }
    }

    @Override
    public List<QuickReport> getQuickReportList() {
        return quickReportMapper.getQuickReportList();
    }

}

