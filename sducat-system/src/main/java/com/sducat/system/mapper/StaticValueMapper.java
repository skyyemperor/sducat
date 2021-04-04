package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.StaticValue;
import org.apache.ibatis.annotations.Param;

/**
 * Created by skyyemperor on 2021-03-09
 * Description :
 */
public interface StaticValueMapper extends BaseMapper<StaticValue> {

    String getValue(String key);

    int setValue(@Param("key") String key,@Param("value") String value);
}