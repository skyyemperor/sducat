package com.sducat.admin.controller;

import com.sducat.common.core.data.dto.UserDto;
import com.sducat.common.core.data.po.SysUser;
import com.sducat.common.core.result.error.AuthError;
import com.sducat.common.core.result.CommonError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.StringUtil;
import com.sducat.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

/**
 * Created by skyyemperor on 2021-02-07 19:49
 * Description :
 */
@RequestMapping("/user")
@RestController
@Validated
public class UserController {

    @Autowired
    private SysUserService userService;

    /**
     * 更新用户信息
     *
     * @param userId   用户ID
     * @param nickName 昵称
     * @param avatar   头像链接
     * @return
     */
    @PreAuthorize("@pms.hasPerm('user:info:*')")
    @PostMapping("/info/update")
    public Result updateUserInfo(@RequestParam("_uid_") Long userId,
                                 @Size(max = 50) @RequestParam String nickName,
                                 @RequestParam String avatar) {
        if (nickName.equals("请登录"))
            nickName = "猫友_" + userId;
        if (StringUtil.isBlank(avatar) && StringUtil.isBlank(nickName))
            return Result.getResult(CommonError.PARAM_FORMAT_WRONG);
        userService.updateUser(new SysUser(userId, nickName, avatar));
        return Result.success();
    }


    /**
     * 获取用户信息(admin)
     */
    @PreAuthorize("@pms.hasPerm('user:info:*')")
    @GetMapping("/info")
    public Result getOtherUserInfo(@RequestParam("_uid_") Long uId,
                                   @RequestParam(required = false) Long userId) {
        if (uId != null) uId = userId; //若otherUserId为空，则获取本人的信息
        UserDto user = userService.getLessUserInfo(uId);
        if (user == null) return Result.getResult(AuthError.USER_NOT_EXIST);
        return Result.success(user);
    }


}
