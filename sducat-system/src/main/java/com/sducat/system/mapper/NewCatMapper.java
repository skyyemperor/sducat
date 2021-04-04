package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.NewCat;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * Created by skyyemperor on 2021-02-28
 * Description :
 */
public interface NewCatMapper extends BaseMapper<NewCat> {
    List<NewCat> selectNewCatList(@Param("offset") Integer offset,
                                  @Param("count") Integer count);
}