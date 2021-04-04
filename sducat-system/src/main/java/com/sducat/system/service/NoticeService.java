package com.sducat.system.service;

import com.sducat.common.core.result.Result;
import com.sducat.system.data.dto.NoticeDto;
import com.sducat.system.data.po.notice.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 在数据库中添加通知
     */
    void addNotice(Notice notice);

    /**
     * 获取通知
     */
    List<NoticeDto> getNoticeList(Long userId,Integer type, Integer pageNum, Integer pageSize);

    /**
     * 设置通知为已读
     */
    Result readNotice(Long userId,Long noticeId);
}
