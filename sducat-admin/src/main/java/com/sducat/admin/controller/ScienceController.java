package com.sducat.admin.controller;

import com.sducat.common.core.result.Result;
import com.sducat.common.util.JSONUtil;
import com.sducat.system.data.po.ScienceModule;
import com.sducat.system.data.po.SciencePoint;
import com.sducat.system.service.ScienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@RequestMapping("/science")
@RestController
@Validated
public class ScienceController {

    @Autowired
    private ScienceService scienceService;

    /**
     * 添加科普板块
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/module/add")
    Result addScienceModule(@RequestParam String moduleName,
                            @RequestParam String moduleMainPic,
                            @RequestParam String moduleSlidePic) {
        ScienceModule module = new ScienceModule(moduleName, moduleMainPic, moduleSlidePic);
        scienceService.addScienceModule(module);
        return Result.success();
    }

    /**
     * 更新科普板块信息
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/module/update")
    Result updateScienceModule(@RequestParam Long moduleId,
                               @RequestParam String moduleName,
                               @RequestParam String moduleMainPic,
                               @RequestParam String moduleSlidePic) {
        ScienceModule module = new ScienceModule(moduleId, moduleName, moduleMainPic, moduleSlidePic);
        scienceService.updateScienceModule(module);
        return Result.success();
    }

    /**
     * 删除科普板块
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/module/delete")
    Result deleteScienceModule(@RequestParam Long moduleId) {
        scienceService.deleteScienceModule(moduleId);
        return Result.success();
    }

    /**
     * 获取全部科普板块信息
     */
    @GetMapping("/module/list")
    Result getScienceModuleList() {
        return Result.success(scienceService.getScienceModuleList());
    }

    /**
     * 添加科普知识点
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/point/add")
    Result addSciencePoint(@RequestParam Long moduleId,
                           @RequestParam String title,
                           @Size(max = 3000) @RequestParam String content) {
        SciencePoint point = new SciencePoint(moduleId, title, content);
        return scienceService.addSciencePoint(point);
    }

    /**
     * 删除科普知识点
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/point/delete")
    Result deleteSciencePoint(@RequestParam Long pointId) {
        scienceService.deleteSciencePoint(pointId);
        return Result.success();
    }

    /**
     * 更新科普知识点
     */
    @PreAuthorize("@pms.hasPerm('science:info:update')")
    @PostMapping("/point/update")
    Result updateSciencePoint(@RequestParam Long pointId,
                              @RequestParam String title,
                              @RequestParam String content) {
        SciencePoint point = new SciencePoint(pointId, null, title, content);
        scienceService.updateSciencePoint(point);
        return Result.success();
    }

    /**
     * 获取该科普板块下的所有知识点
     */
    @GetMapping("/point/list")
    Result getSciencePointList(Long moduleId) {
        return Result.success(scienceService.getSciencePointList(moduleId));
    }

}
