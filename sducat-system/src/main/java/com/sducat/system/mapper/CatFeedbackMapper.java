package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.dto.FeedbackDto;
import com.sducat.system.data.po.Feedback;
import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
public interface CatFeedbackMapper extends BaseMapper<Feedback> {
    List<FeedbackDto> getFeedbackList(@Param("offset") Integer offset,
                                      @Param("count") Integer count);
}