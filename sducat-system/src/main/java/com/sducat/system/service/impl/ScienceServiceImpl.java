package com.sducat.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.ScienceError;
import com.sducat.system.data.po.ScienceModule;
import com.sducat.system.mapper.ScienceModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.mapper.SciencePointMapper;
import com.sducat.system.data.po.SciencePoint;
import com.sducat.system.service.ScienceService;

import java.util.List;

@Service
public class ScienceServiceImpl extends ServiceImpl<SciencePointMapper, SciencePoint> implements ScienceService {

    @Autowired
    private ScienceModuleMapper scienceModuleMapper;

    @Autowired
    private SciencePointMapper sciencePointMapper;

    @Override
    public void addScienceModule(ScienceModule module) {
        scienceModuleMapper.insert(module);
    }

    @Override
    public void updateScienceModule(ScienceModule module) {
        scienceModuleMapper.updateById(module);
    }

    @Override
    public void deleteScienceModule(Long moduleId) {
        scienceModuleMapper.deleteById(moduleId);
    }

    @Override
    public List<ScienceModule> getScienceModuleList() {
        return scienceModuleMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Result addSciencePoint(SciencePoint point) {
        if (scienceModuleMapper.selectById(point.getModuleId()) == null)
            return Result.getResult(ScienceError.SCIENCE_MODULE_NOT_EXIST);
        sciencePointMapper.insert(point);
        return Result.success();
    }

    @Override
    public void deleteSciencePoint(Long pointId) {
        sciencePointMapper.deleteById(pointId);
    }

    @Override
    public void updateSciencePoint(SciencePoint point) {
        sciencePointMapper.updateById(point);
    }

    @Override
    public List<SciencePoint> getSciencePointList(Long moduleId) {
        return sciencePointMapper.selectPointList(moduleId);
    }
}
