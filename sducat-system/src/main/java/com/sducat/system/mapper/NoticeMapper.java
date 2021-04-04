package com.sducat.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sducat.system.data.po.notice.Notice;
import com.sducat.system.data.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
public interface NoticeMapper extends BaseMapper<Notice> {
    List<NoticeVo> selectNoticeVoList(@Param("userId") Long userId,
                                      @Param("type") Integer type,
                                      @Param("offset") Integer offset,
                                      @Param("count") Integer count);
}