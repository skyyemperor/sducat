package com.sducat.system.service.impl;

import com.sducat.common.constant.Constants;
import com.sducat.common.core.redis.RedisCache;
import com.sducat.common.core.result.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CatError;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.common.util.StringUtil;
import com.sducat.common.util.TaskExecutorUtil;
import com.sducat.system.data.dto.CatAdoptDto;
import com.sducat.system.data.dto.CatLessDto;
import com.sducat.system.data.dto.FeedbackDto;
import com.sducat.system.data.dto.NewCatDto;
import com.sducat.system.data.po.Feedback;
import com.sducat.system.data.po.NewCat;
import com.sducat.system.data.po.notice.*;
import com.sducat.system.mapper.CatFeedbackMapper;
import com.sducat.system.mapper.CatRelationMapper;
import com.sducat.system.mapper.NewCatMapper;
import com.sducat.system.service.NoticeService;
import com.sducat.system.service.SysUserService;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.mapper.CatMapper;
import com.sducat.system.data.po.Cat;
import com.sducat.system.service.CatService;

/**
 * Created by skyyemperor on 2021-01-30 16:55
 * Description :
 */
@Service
public class CatServiceImpl extends ServiceImpl<CatMapper, Cat> implements CatService {

    @Autowired
    private CatMapper catMapper;

    @Autowired
    private CatRelationMapper relationMapper;

    @Autowired
    private CatFeedbackMapper feedbackMapper;

    @Autowired
    private NewCatMapper newCatMapper;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Cat getCatInfo(Long catId) {
        Cat cat;
        String catInfoRedisKey = Constants.CAT_IFO_REDIS_KEY + catId;
        //首先到redis查询
        if ((cat = redisCache.getObject(catInfoRedisKey)) != null) return cat;

        cat = catMapper.selectCatByCatId(catId);

        //异步更新redis
        redisCache.setObjectAsync(catInfoRedisKey, cat);
        return cat;
    }

    @Override
    public boolean addCat(Cat cat) {
        return this.save(cat);
    }

    @Override
    public boolean updateCat(Cat cat) {
        String catInfoRedisKey = Constants.CAT_IFO_REDIS_KEY + cat.getCatId();
        boolean ans = this.updateById(cat); //1.删除数据库数据
        redisCache.deleteObject(catInfoRedisKey); //2.后删缓存
        taskExecutorUtil.run(() -> {  //3.延时删除缓存。防止双写不一致情况发生
            try {
                Thread.sleep(1000); //睡1秒
            } catch (InterruptedException e) { //to do nothing
            }
            redisCache.deleteObject(catInfoRedisKey);
        });
        return ans;
    }

    @Override
    public List<CatLessDto> searchCat(String catName, String campus, CatStatusEnum status, String color, Integer pageNum, Integer pageSize) {
        if (catName != null && !"".equals(catName)) {
            //在字符中间隔插入'%',模糊搜索
            catName = catName.replaceAll(" ", "");
            catName = "%" + StringUtil.join(catName.toCharArray(), "%") + "%";
        }

        List<CatLessDto> catLessDtos;
        String searchCatRedisKey = Constants.SEARCH_CAT_REDIS_KEY + catName + campus + status + color + pageNum + "," + pageSize;
        //首先在redis中查询
        if ((catLessDtos = redisCache.getObject(searchCatRedisKey)) != null) return catLessDtos;

        List<Cat> cats = catMapper.searchCat(catName, campus, status, color, (pageNum - 1) * pageSize, pageSize);
        catLessDtos = cats.stream().map(CatLessDto::getDto).collect(Collectors.toList());

        //将结果存入redis
        redisCache.setObjectAsync(searchCatRedisKey, catLessDtos, 2, TimeUnit.DAYS);
        return catLessDtos;
    }

    @Override
    public List<CatAdoptDto> getAdoptCatList(Integer pageNum, Integer pageSize) {
        List<Cat> cats = catMapper.selectCatAdoptList((pageNum - 1) * pageSize, pageSize);
        return cats.stream().map(CatAdoptDto::getDto).collect(Collectors.toList());
    }

    @Override
    public void addCatRelation(Long firstCatId, Long secondCatId) {
        //插入两次，两次相反
        if (relationMapper.judgeRelation(firstCatId, secondCatId) == null)
            relationMapper.addRelation(firstCatId, secondCatId);

        if (relationMapper.judgeRelation(secondCatId, firstCatId) == null)
            relationMapper.addRelation(secondCatId, firstCatId);
    }

    @Override
    public void deleteCatRelation(Long firstCatId, Long secondCatId) {
        relationMapper.deleteRelation(firstCatId, secondCatId);
        relationMapper.deleteRelation(secondCatId, firstCatId);
    }

    @Override
    public Result submitFeedback(Feedback feedback) {
        Cat cat = getCatInfo(feedback.getCatId());
        if (cat == null) return Result.getResult(CatError.CAT_IS_NOT_EXIST);
        feedbackMapper.insert(feedback);
        //已收到审核表单的通知
        noticeService.addNotice(new CatFeedbackReceivedNotice(
                feedback.getUserId(), feedback.getCatId().toString(), cat.getCatName()));
        return Result.success();
    }

    @Override
    public List<FeedbackDto> getFeedbackList(Integer pageNum, Integer pageSize) {
        return feedbackMapper.getFeedbackList((pageNum - 1) * pageSize, pageSize);
    }

    @Override
    public Result checkFeedback(Long feedbackId, String reason, boolean checked) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) return Result.getResult(CatError.FEEDBACK_NOT_EXIST);
        if (feedback.getChecked() != Constants.WAIT_CHECK) return Result.getResult(CommonError.HAD_CHECKED);

        Cat cat = getCatInfo(feedback.getCatId());
        if (checked) {
            feedback.setChecked(Constants.CHECK_SUCCESS);
            feedbackMapper.updateById(feedback);
            noticeService.addNotice(new CatFeedbackCheckSuccessNotice(
                    feedback.getUserId(), feedback.getCatId().toString(), cat.getCatName()
            ));
        } else {
            feedback.setChecked(Constants.CHECK_FAIL);
            feedbackMapper.updateById(feedback);
            noticeService.addNotice(new CatFeedbackCheckFailNotice(
                    feedback.getUserId(), feedback.getCatId().toString(), cat.getCatName(), reason
            ));
        }
        return Result.success();
    }

    @Override
    public void submitNewCat(NewCat cat) {
        newCatMapper.insert(cat);
        taskExecutorUtil.run(() -> noticeService.addNotice(
                new NewCatFormReceivedNotice(cat.getSubmitUserId(), null)
        ));
    }

    @Override
    public List<NewCatDto> getNewCatList(Integer pageNum, Integer pageSize) {
        List<NewCat> newCatList = newCatMapper.selectNewCatList((pageNum - 1) * pageSize, pageSize);
        List<NewCatDto> newCatDtoList = newCatList.stream().map(NewCatDto::getDto).collect(Collectors.toList());
        for (NewCatDto cat : newCatDtoList) {
            cat.setSubmitUserName(userService.getLessUserInfo(Long.parseLong(cat.getSubmitUserName())).getNickName());
        }
        return newCatDtoList;
    }

    @Override
    public Result checkNewCat(Long newCatId, Long catId, boolean checked) {
        NewCat newCat = newCatMapper.selectById(newCatId);
        if (newCat == null) return Result.getResult(CatError.NEW_CAT_NOT_EXIST);
        if (newCat.getChecked() != Constants.WAIT_CHECK) return Result.getResult(CommonError.HAD_CHECKED);

        if (checked) {
            newCat.setChecked(Constants.CHECK_SUCCESS); //审核成功
            newCatMapper.updateById(newCat);
            noticeService.addNotice(new NewCatCheckSuccessNotice(
                    newCat.getSubmitUserId(), catId.toString(), newCat.getSubmitTime()
            ));
        } else {
            newCat.setChecked(Constants.CHECK_FAIL); //审核失败
            newCatMapper.updateById(newCat);
            noticeService.addNotice(new NewCatCheckFailNotice(
                    newCat.getSubmitUserId(), null, newCat.getSubmitTime()
            ));
        }
        return Result.success();
    }

}


