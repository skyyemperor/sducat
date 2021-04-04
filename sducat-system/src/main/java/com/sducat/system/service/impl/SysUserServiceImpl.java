package com.sducat.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.common.core.data.dto.UserDto;
import com.sducat.common.core.data.po.SysRole;
import com.sducat.common.core.data.po.SysUser;
import com.sducat.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sducat.system.service.SysUserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by skyyemperor on 2021-01-28 22:00
 * Description :
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SysUser getUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    public SysUser getUserByUserId(Long userId) {
        return userMapper.selectUserByUserId(userId);
    }

    @Override
    public Set<String> getRolesByUserId(Long userId) {
        List<SysRole> roles = userMapper.selectRolesByUserId(userId);
        Set<String> roleSet = new HashSet<>();
        for (SysRole role : roles) {
            if (role != null) {
                roleSet.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
            }
        }
        return roleSet;
    }

    @Override
    public Set<String> getPermsByUserId(Long userId) {
        return new HashSet<>(userMapper.selectPermsByUserId(userId));
    }

    @Override
    public UserDto getLessUserInfo(Long userId) {
        return userMapper.selectLessUserInfoByUserId(userId);
    }

    @Override
    public boolean updateUser(SysUser user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    public SysUser register(SysUser user) {
        this.save(user);
        user = userMapper.selectUserByUserName(user.getUserName());
        userMapper.insertUserRoleLink(user.getUserId(), 3L); //roleId为3代表普通用户
        return user;
    }
}

