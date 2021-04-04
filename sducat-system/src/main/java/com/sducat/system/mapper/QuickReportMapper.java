package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.QuickReport;import java.util.List;

/**
 * Created by skyyemperor on 2021-02-23
 * Description :
 */
public interface QuickReportMapper extends BaseMapper<QuickReport> {
    void setAllReportsInvalid();

    List<QuickReport> getQuickReportList();
}