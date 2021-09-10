package com.sducat.framework.service;

import com.sducat.common.core.data.po.SysUser;
import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.util.StringUtil;
import com.sducat.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户验证处理
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getUserByUserName(username);
        if (StringUtil.isNull(user)) {
            //首次登录，数据库中不存在，新插入一个用户
            user = userService.register(new SysUser(username, "神秘猫友"));
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, userService.getPermsByUserId(user.getUserId()));
    }
}
