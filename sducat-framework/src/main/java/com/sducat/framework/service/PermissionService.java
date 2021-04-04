package com.sducat.framework.service;

import com.sducat.common.core.data.po.SysRole;
import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.core.exception.user.NoPermissionException;
import com.sducat.common.core.exception.user.UnauthorizedException;
import com.sducat.common.util.ServletUtil;
import com.sducat.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Service("pms")
public class PermissionService {
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 超级管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "super_admin";

    private static final String ROLE_DELIMITER = ",";

    private static final String PERMISSION_DELIMITER = ",";

    @Autowired
    private TokenService tokenService;

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPerm(String permission) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtil.getRequest());
        //token失效
        if (StringUtil.isNull(loginUser)) throw new UnauthorizedException();

        if (CollectionUtils.isEmpty(loginUser.getPermissions()) ||
                !hasPermissions(loginUser.getPermissions(), permission)) {
            throw new NoPermissionException(); //无权限访问
        }

        return true;
    }

    /**
     * 验证用户是否不具备某权限，与 hasPerm逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPerm(String permission) {
        return !hasPerm(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_NAMES_DELIMITER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPerm(String permissions) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtil.getRequest());
        //token失效
        if (StringUtil.isNull(loginUser)) throw new UnauthorizedException();

        if (CollectionUtils.isEmpty(loginUser.getPermissions()))
            throw new NoPermissionException();

        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(PERMISSION_DELIMITER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }

        throw new NoPermissionException(); //无权限访问
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtil.getRequest());
        //token失效
        if (StringUtil.isNull(loginUser)) throw new UnauthorizedException();

        if (CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            throw new NoPermissionException(); //无权限访问
        }
        for (SysRole sysRole : loginUser.getUser().getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtil.trim(role))) {
                return true;
            }
        }
        throw new NoPermissionException(); //无权限访问
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtil.getRequest());
        //token失效
        if (StringUtil.isNull(loginUser)) throw new UnauthorizedException();

        if (CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            throw new NoPermissionException(); //无权限访问
        }

        for (String role : roles.split(ROLE_DELIMITER)) {
            if (hasRole(role)) {
                return true;
            }
        }
        throw new NoPermissionException(); //无权限访问
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtil.trim(permission));
    }
}
