package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.CatRelation;
import org.apache.ibatis.annotations.Param;

/**
 * Created by skyyemperor on 2021-02-07 21:57
 * Description :
 */
public interface CatRelationMapper extends BaseMapper<CatRelation> {

    CatRelation judgeRelation(@Param("catId") Long catId,
                              @Param("relativeId") Long relativeId);

    Integer addRelation(@Param("catId") Long catId,
                        @Param("relativeId") Long relativeId);

    Integer deleteRelation(@Param("catId") Long catId,
                           @Param("relativeId") Long relativeId);

}