package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.SciencePoint;import java.util.List;

public interface SciencePointMapper extends BaseMapper<SciencePoint> {
    List<SciencePoint> selectPointList(Long moduleId);
}