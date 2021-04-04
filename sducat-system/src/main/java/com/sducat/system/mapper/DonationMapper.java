package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.Donation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by skyyemperor on 2021-03-09
 * Description :
 */
public interface DonationMapper extends BaseMapper<Donation> {
    List<Donation> selectDonationList(@Param("year") Integer year,
                                      @Param("month") Integer month);
}