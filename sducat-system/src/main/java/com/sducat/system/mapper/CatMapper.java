package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.system.data.dto.CatAdoptDto;
import com.sducat.system.data.dto.CatLessDto;
import com.sducat.system.data.po.Cat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by skyyemperor on 2021-01-30 17:00
 * Description :
 */
public interface CatMapper extends BaseMapper<Cat> {
    Cat selectCatByCatId(Long catId);

    List<Cat> searchCat(@Param("catName") String catName,
                        @Param("campus") String campus,
                        @Param("status") CatStatusEnum status,
                        @Param("color") String color,
                        @Param("offset") Integer offset,
                        @Param("count") Integer count);

    List<Cat> selectCatAdoptList(@Param("offset") Integer offset,
                                 @Param("count") Integer count);
}