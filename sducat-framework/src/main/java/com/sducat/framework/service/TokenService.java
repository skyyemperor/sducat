package com.sducat.framework.service;

import com.sducat.common.constant.Constants;
import com.sducat.common.core.data.po.SysUser;
import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.core.redis.RedisCache;
import com.sducat.common.util.ServletUtil;
import com.sducat.common.util.StringUtil;
import com.sducat.common.util.TaskExecutorUtil;
import com.sducat.common.util.ip.IpUtils;
import com.sducat.common.util.uuid.IdUtil;
import com.sducat.system.service.SysUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 */
@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌有效期（默认120分钟）
    @Value("${token.token_expire_time}")
    private int tokenExpireTime;

    // refresh_token有效期（默认60天）
    @Value("${token.refresh_token_expire_time}")
    private int refreshTokenExpireTime;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    @Autowired
    private SysUserService userService;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtil.isNotEmpty(token)) {
            LoginUser user = redisCache.getObject(getTokenRedisKey(token));
            if (StringUtil.isNotNull(user)) {
                //后台刷新
                taskExecutorUtil.run(() -> {
                    refreshRedisToken(user, false);
                });
            }
            return user;
        }
        return null;
    }

    /**
     * 登录
     */
    public String[] login(LoginUser loginUser) {
        //不强制删除未失效的token
        return createToken(loginUser, false);
    }

    /**
     * 刷新token
     */
    public String[] refresh(String refreshToken) {
        LoginUser user = redisCache.getObject(getRefreshTokenRedisKey(refreshToken));
        if (user == null) return new String[]{};
        //强制删除未失效的token
        return createToken(user, true);
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String[] createToken(LoginUser loginUser, boolean isDeleteOld) {
        String token = null, refresh_token = null;
        if (!isDeleteOld) {
            String[] str = getToken(loginUser.getUser().getUserId());
            if (str.length == 2) {
                token = str[0];
                refresh_token = str[1];
            }
        }
        if (token == null || refresh_token == null) {
            token = IdUtil.fastUUID();
            refresh_token = IdUtil.fastUUID();
        }
        loginUser.setToken(token); //设置token
        loginUser.setRefreshToken(refresh_token);
        setUserAgent(loginUser); //设置登录ip和登录地址等信息

        deleteOldAndSetNewUserInfo(loginUser); //删除老的token和refresh_token
        refreshRedisToken(loginUser, true); //刷新token在redis中的有效时间

        return new String[]{token, refresh_token};
    }

    /**
     * 删除用户身份信息
     */
    public void logout(String token) {
        redisCache.deleteObject(getTokenRedisKey(token));
    }


    /**
     * 刷新令牌有效期
     *
     * @param loginUser      登录信息
     * @param isForceRefresh 是否是强制刷新
     */
    private void refreshRedisToken(LoginUser loginUser, boolean isForceRefresh) {
        if (!isForceRefresh && loginUser.getLoginTime() != null) {
            long loginTime = loginUser.getLoginTime();
            long currentTime = System.currentTimeMillis();
            if (currentTime - loginTime < tokenExpireTime / 10 * 60 * 1000) {
                return; //若当前时间减去登录时的时间小于token有效期的1/10，则不刷新
            }
        }

        //设置新的时间
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + tokenExpireTime * 60 * 1000);

        //刷新数据库中的用户登录信息
        SysUser sysUser = loginUser.getUser();
        sysUser.setLoginDate(new Date());
        if (loginUser.getIpaddr() != null)
            sysUser.setLoginIp(loginUser.getIpaddr());
        userService.updateUser(sysUser);

        //更新token有效期
        redisCache.setObject(getTokenRedisKey(loginUser.getToken()), loginUser,
                tokenExpireTime, TimeUnit.MINUTES);
        //更新refresh_token有效期
        redisCache.setObject(getRefreshTokenRedisKey(loginUser.getRefreshToken()), loginUser,
                refreshTokenExpireTime, TimeUnit.DAYS);
        redisCache.setObject(getUserInfoRedisKey(loginUser.getUser().getUserId()), loginUser);
    }

    private void deleteOldAndSetNewUserInfo(LoginUser loginUser) {
        String userInfoRedisKey = getUserInfoRedisKey(loginUser.getUser().getUserId());
        LoginUser oldUser = redisCache.getObject(userInfoRedisKey);
        if (oldUser != null) {
            //删除old的token和refresh_token
            redisCache.deleteObject(getTokenRedisKey(oldUser.getToken()));
            redisCache.deleteObject(getRefreshTokenRedisKey(oldUser.getRefreshToken()));
        }
        redisCache.setObject(userInfoRedisKey, loginUser);
    }

    public Long getUserId() {
        LoginUser user = getLoginUser(ServletUtil.getRequest());
        if (user == null) return null;
        return user.getUser().getUserId();
    }

    public String[] getToken(Long userId) {
        LoginUser user = redisCache.getObject(getUserInfoRedisKey(userId));
        if (user == null || user.getExpireTime() == null ||
                user.getExpireTime() < System.currentTimeMillis()) return new String[]{};
        else return new String[]{user.getToken(), user.getRefreshToken()};
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    private void setUserAgent(LoginUser loginUser) {
        String ip = IpUtils.getIpAddr(ServletUtil.getRequest());
        loginUser.setIpaddr(ip);
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        return request.getHeader(header);
    }

    private String getTokenRedisKey(String uuid) {
        return Constants.TOKEN_REDIS_KEY + uuid;
    }

    private String getRefreshTokenRedisKey(String uuid) {
        return Constants.REFRESH_TOKEN_REDIS_KEY + uuid;
    }

    private String getUserInfoRedisKey(Long userId) {
        return Constants.USER_INFO_REDIS_KEY + userId;
    }
}
