package com.sducat.admin.controller;

import com.sducat.common.annotation.validation.EnumValidation;
import com.sducat.common.constant.Constants;
import com.sducat.common.core.result.error.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.core.result.error.CatError;
import com.sducat.common.enums.CampusEnum;
import com.sducat.common.enums.CatStatusEnum;
import com.sducat.common.util.ExcelUtil;
import com.sducat.common.util.JSONUtil;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.common.util.StringUtil;
import com.sducat.system.data.dto.CatDto;
import com.sducat.system.data.dto.CatLessDto;
import com.sducat.system.data.dto.FeedbackDto;
import com.sducat.system.data.dto.NewCatDto;
import com.sducat.system.data.po.Cat;
import com.sducat.system.data.po.Feedback;
import com.sducat.system.data.po.NewCat;
import com.sducat.system.service.CatService;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Created by skyyemperor on 2021-01-30 17:21
 * Description :
 */
@RestController
@RequestMapping("/cat")
@Validated
public class CatController {

    @Autowired
    private CatService catService;

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    /**
     * 获取猫咪详细信息
     */
    @GetMapping("/info/{catId}")
    public Result getCatInfo(@PathVariable Long catId) {
        Cat cat = catService.getCatInfo(catId);
        if (StringUtil.isNull(cat)) {
            return Result.getResult(CatError.CAT_IS_NOT_EXIST);
        }
        return Result.success(CatDto.getDto(cat));
    }

    /**
     * 添加猫咪
     */
    @PreAuthorize("@pms.hasPerm('cat:info:update')")
    @PostMapping("/add")
    public Result addCat(@Validated(Insert.class) @RequestBody Cat cat) {
        Result checkRes = checkCatParam(cat);
        if (checkRes.getCode() != 0) return checkRes;

        catService.addCat(cat);
        return Result.success();
    }

    /**
     * 更新猫咪信息
     */
    @PreAuthorize("@pms.hasPerm('cat:info:update')")
    @PostMapping("/update")
    public Result updateCatInfo(@Validated(Update.class) @RequestBody Cat cat) {
        Result checkRes = checkCatParam(cat);
        if (checkRes.getCode() != 0) return checkRes;

        catService.updateCat(cat);
        return Result.success();
    }

    /**
     * 筛选猫咪
     */
    @GetMapping("/search")
    public Result searchCat(@RequestParam(required = false) String catName,
                            @EnumValidation(clazz = CampusEnum.class, message = "没有此校区哦")
                            @RequestParam(required = false) String campus,
                            @EnumValidation(clazz = CatStatusEnum.class, message = "没有这个在校状态哦")
                            @RequestParam(required = false) Integer status,
                            @RequestParam(required = false) String color,
                            @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                            @Min(0) @RequestParam(defaultValue = "20") Integer pageSize) {
        List<CatLessDto> cats = catService.searchCat(catName, campus, CatStatusEnum.getEnum(status), color, pageNum, pageSize);
        return Result.success(cats);
    }

    @GetMapping("/adopt/list")
    public Result getAdoptCatList(@Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                                  @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(catService.getAdoptCatList(pageNum, pageSize));
    }

    /**
     * 添加猫咪社会关系
     */
    @PreAuthorize("@pms.hasPerm('cat:info:update')")
    @PostMapping("/relation/add")
    public Result addCatRelation(@RequestParam Long firstCatId,
                                 @RequestParam Long secondCatId) {
        if (firstCatId.equals(secondCatId))
            return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
        catService.addCatRelation(firstCatId, secondCatId);
        return Result.success();
    }

    /**
     * 删除猫咪社会关系
     */
    @PreAuthorize("@pms.hasPerm('cat:info:update')")
    @PostMapping("/relation/delete")
    public Result deleteCatRelation(@RequestParam Long firstCatId,
                                    @RequestParam Long secondCatId) {
        if (firstCatId.equals(secondCatId))
            return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
        catService.deleteCatRelation(firstCatId, secondCatId);
        return Result.success();
    }

    /**
     * 提交反馈
     */
    @PreAuthorize("@pms.hasPerm('cat:feedback:submit')")
    @PostMapping("/feedback/submit")
    public Result submitFeedback(
            @RequestParam("_uid_") Long userId,
            @RequestParam Long catId,
            @Size(max = 300, message = "反馈内容字数最多不能超过300字哦") @RequestParam String content,
            @Size(max = 100, message = "联系方式最多不能超过100字哦") @RequestParam String contactWay) {
        Feedback feedback = new Feedback(catId, userId, content, contactWay, new Date(), Constants.WAIT_CHECK);
        catService.submitFeedback(feedback);
        return Result.success();
    }

    /**
     * 通过年月获取反馈list(admin)
     */
    @PreAuthorize("@pms.hasPerm('cat:feedback:check')")
    @GetMapping("/feedback/list")
    public Result getFeedbackList(@Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
                                  @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        List<FeedbackDto> feedbackList = catService.getFeedbackList(pageNum, pageSize);
        if (feedbackList.size() == 0)
            return Result.getResult(CommonError.THIS_IS_LAST_PAGE);
        return Result.success(feedbackList);
    }

    /**
     * 审核反馈
     *
     * @param feedbackId 反馈ID
     * @param reason     反馈未通过的原因
     * @param checked    是否通过
     */
    @PreAuthorize("@pms.hasPerm('cat:feedback:check')")
    @PostMapping("/feedback/check")
    public Result checkFeedback(@RequestParam Long feedbackId,
                                @RequestParam(defaultValue = "") String reason,
                                @RequestParam boolean checked) {
        return catService.checkFeedback(feedbackId, reason, checked);
    }

    /**
     * 报送新猫
     */
    @PreAuthorize("@pms.hasPerm('cat:new:submit')")
    @PostMapping("/new/submit")
    public Result submitNewCat(
            @RequestParam("_uid_") Long userId,
            @EnumValidation(clazz = CampusEnum.class, message = "没有此校区哦") @RequestParam String campus,
            @Size(max = 100) @RequestParam String witnessLocation,
            @Size(max = 100) @RequestParam String witnessTime,
            @Size(max = 140) @RequestParam String description,
            @RequestParam String[] pic) {
        if (pic.length > Constants.PIC_MAX_NUM)
            return Result.getResult(CommonError.PIC_NUM_EXCEED_MAX);
        if (!qiNiuYunPicUtil.checkPicDomain(pic))
            return Result.getResult(CommonError.PIC_DOMAIN_WRONG);

        NewCat cat = new NewCat(userId, campus, witnessLocation, witnessTime, description,
                JSONUtil.toJson(pic), new Date(), Constants.WAIT_CHECK);
        catService.submitNewCat(cat);
        return Result.success();
    }

    @PreAuthorize("@pms.hasPerm('cat:new:check')")
    @GetMapping("/new/list")
    public Result getNewCatList(
            @Min(1) @RequestParam(defaultValue = "1") Integer pageNum,
            @Min(0) @RequestParam(defaultValue = "10") Integer pageSize) {
        List<NewCatDto> newCatList = catService.getNewCatList(pageNum, pageSize);
        if (newCatList.size() == 0)
            return Result.getResult(CommonError.THIS_IS_LAST_PAGE);
        return Result.success(newCatList);
    }


    /**
     * 审核新猫
     *
     * @param newCatId 新猫ID
     * @param catId    已上新的猫咪ID
     * @param checked  审核是否通过
     */
    @PreAuthorize("@pms.hasPerm('cat:new:check')")
    @PostMapping("/new/check")
    Result checkNewCat(@RequestParam Long newCatId,
                       @RequestParam(required = false) Long catId,
                       @RequestParam boolean checked) {
        if (checked && catId == null)
            return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
        return catService.checkNewCat(newCatId, catId, checked);
    }

    /**
     * 检查图片格式
     */
    private Result checkCatParam(Cat cat) {
        List<String> pics = new ArrayList<>();
        if (cat.getSlidePic() != null) {
            try {
                pics = JSONUtil.getStringArray(cat.getSlidePic());
                if (pics.size() > Constants.PIC_MAX_NUM)
                    return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
            } catch (Exception e) {
                return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
            }
        }
        pics.add(cat.getMainPic());
        pics.add(cat.getRoundPic());
        for (String pic : pics) {
            if (!qiNiuYunPicUtil.checkPicDomain(pic))
                return Result.getResult(CommonError.PIC_DOMAIN_WRONG);
        }
        return Result.success();
    }

}
