package com.sducat.system.service;

import com.sducat.common.core.result.Result;
import com.sducat.system.data.po.ScienceModule;
import com.sducat.system.data.po.SciencePoint;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ScienceService extends IService<SciencePoint> {

    /**
     * 添加科普板块
     */
    void addScienceModule(ScienceModule module);

    /**
     * 更新科普板块信息
     */
    void updateScienceModule(ScienceModule module);

    /**
     * 删除科普板块
     */
    void deleteScienceModule(Long moduleId);

    /**
     * 获取全部科普板块信息
     */
    List<ScienceModule> getScienceModuleList();

    /**
     * 添加科普知识点
     */
    Result addSciencePoint(SciencePoint point);

    /**
     * 删除科普知识点
     */
    void deleteSciencePoint(Long pointId);

    /**
     * 更新科普知识点
     */
    void updateSciencePoint(SciencePoint point);

    /**
     * 获取该科普板块下的所有知识点
     */
    List<SciencePoint> getSciencePointList(Long moduleId);
}
