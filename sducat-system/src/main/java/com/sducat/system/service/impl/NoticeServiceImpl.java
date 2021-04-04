package com.sducat.system.service.impl;

import com.sducat.common.constant.Constants;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.core.result.error.NoticeError;
import com.sducat.common.util.TaskExecutorUtil;
import com.sducat.system.data.dto.NoticeDto;
import com.sducat.system.data.vo.NoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.mapper.NoticeMapper;
import com.sducat.system.data.po.notice.Notice;
import com.sducat.system.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by skyyemperor on 2021-02-25
 * Description :
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    @Override
    public void addNotice(Notice notice) {
        taskExecutorUtil.run(() -> noticeMapper.insert(notice));
    }

    @Override
    public List<NoticeDto> getNoticeList(Long userId,Integer type, Integer pageNum, Integer pageSize) {
        List<NoticeVo> noticeVoList = noticeMapper.selectNoticeVoList(userId,type, (pageNum - 1) * pageSize, pageSize);
        return noticeVoList.stream().map(NoticeDto::getDto).collect(Collectors.toList());
    }

    @Override
    public Result readNotice(Long userId, Long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        if (notice == null) return Result.getResult(NoticeError.NOTICE_NOT_EXIST);
        if (!notice.getUserId().equals(userId)) return Result.getResult(CommonError.PERM_NOT_ALLOW);

        notice.setRead(Constants.YES);
        noticeMapper.updateById(notice);
        return Result.success();
    }

}
