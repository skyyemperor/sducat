package com.sducat.system.service;

import com.sducat.common.core.result.Result;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.system.data.dto.CatAdoptDto;
import com.sducat.system.data.dto.CatLessDto;
import com.sducat.system.data.dto.FeedbackDto;
import com.sducat.system.data.dto.NewCatDto;
import com.sducat.system.data.po.Cat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sducat.system.data.po.Feedback;
import com.sducat.system.data.po.NewCat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by skyyemperor on 2021-01-30 16:55
 * Description :
 */
public interface CatService extends IService<Cat> {

    Cat getCatInfo(Long catId);

    boolean addCat(Cat cat);

    boolean updateCat(Cat cat);

    List<CatLessDto> searchCat(String catName, String campus, CatStatusEnum status, String color, Integer pageNum, Integer pageSize);

    /**
     * 获取待领养猫咪列表
     */
    List<CatAdoptDto> getAdoptCatList(Integer pageNum, Integer pageSize);

    /**
     * 添加猫咪关系
     */
    void addCatRelation(Long firstCatId, Long secondCatId);

    /**
     * 删除猫咪关系
     */
    void deleteCatRelation(Long firstCatId, Long secondCatId);

    /**
     * 反馈猫咪信息
     */
    Result submitFeedback(Feedback feedback);

    /**
     * 获取猫咪反馈list
     */
    List<FeedbackDto> getFeedbackList(Integer pageNum, Integer pageSize);

    /**
     * 审核反馈
     *
     * @param feedbackId 反馈ID
     * @param reason     反馈未通过的原因
     * @param checked    是否通过
     */
    Result checkFeedback(Long feedbackId, String reason, boolean checked);

    /**
     * 报送新猫
     */
    void submitNewCat(NewCat cat);

    /**
     * 获取待审核新猫
     */
    List<NewCatDto> getNewCatList(Integer pageNum, Integer pageSize);

    /**
     * 审核新猫
     *
     * @param newCatId 新猫ID
     * @param catId    已上新的猫咪ID
     * @param checked  审核是否通过
     */
    Result checkNewCat(Long newCatId, Long catId, boolean checked);
}


