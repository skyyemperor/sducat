package com.sducat.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sducat.common.core.data.dto.UserDto;
import com.sducat.common.core.data.po.SysUser;

import java.util.Set;

/**
 * Created by skyyemperor on 2021-01-28 22:00
 * Description :
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getUserByUserName(String userName);

    SysUser getUserByUserId(Long userId);

    Set<String> getRolesByUserId(Long userId);

    Set<String> getPermsByUserId(Long userId);

    UserDto getLessUserInfo(Long userId);

    boolean updateUser(SysUser user);

    SysUser register(SysUser user);
}

