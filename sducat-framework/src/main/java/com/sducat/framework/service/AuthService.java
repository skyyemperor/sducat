package com.sducat.framework.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.StringUtil;
import com.sducat.common.util.http.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 */
@Component
@Slf4j
public class AuthService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Value("${admin-user.username}")
    private String adminUserName;

    @Value("${admin-user.password}")
    private String adminPassword;

    @Value("${weixin_app.app_id}")
    private String appId;

    @Value("${weixin_app.app_secret}")
    private String appSecret;

    public static final String WX_LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 登录验证
     *
     * @return 结果
     */
    public String[] login(String userCode) {
        //调用微信接口获取用户openId
        String userOpenId = wxLogin(userCode);

        //请求失败，返回null
        if (StringUtil.isNull(userOpenId)) return null;

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userOpenId, ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.login(loginUser);
    }

    public String[] adminLogin(String userName, String password) {
        if (!adminUserName.equals(userName) || !adminPassword.equals(password))
            return null;

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userName, ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.login(loginUser);
    }


    /**
     * refresh Token
     *
     * @param refreshToken
     * @return
     */
    public String[] refresh(String refreshToken) {
        return tokenService.refresh(refreshToken);
    }


    private String wxLogin(String code) {
        String response = OkHttpUtil.getInstance().getData(WX_LOGIN_API,
                MapBuildUtil.builder()
                        .data("appid", appId)
                        .data("secret", appSecret)
                        .data("js_code", code)
                        .data("grant_type", "authorization_code")
                        .get(),
                null);

        if (response != null) {
            JSONObject jsonObject = JSON.parseObject(response);
            log.info("[login: ]" + response);
            return jsonObject.getString("openid");
        }

        //登录失败返回null
        return null;
    }
}
