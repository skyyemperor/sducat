package com.sducat.admin.controller;

import com.sducat.common.core.redis.RedisCache;
import com.sducat.common.core.result.error.AuthError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.QiNiuYunPicUtil;
import com.sducat.framework.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Created by skyyemperor on 2021-01-29 0:09
 * Description :
 */
@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @Autowired
    private QiNiuYunPicUtil qiNiuYunPicUtil;

    @Autowired
    private RedisCache redisCache;

//    @RequestMapping("/test")
//    public String test() {
//        return "test";
//    }

    /**
     * 登录方法
     */
    @PostMapping("/login")
    public Result login(@RequestParam String code) {
        String[] tokenAndRefreshToken = authService.login(code);
        if (tokenAndRefreshToken == null || tokenAndRefreshToken.length < 2) {
            return Result.getResult(AuthError.LOGIN_FAIL);
        }

        return Result.success(MapBuildUtil.builder()
                .data("token", tokenAndRefreshToken[0])
                .data("refresh_token", tokenAndRefreshToken[1])
                .get()
        );
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public Result adminLogin(@RequestParam String userName,
                             @RequestParam String password) {
        String[] tokenAndRefreshToken = authService.adminLogin(userName, password);
        if (tokenAndRefreshToken == null || tokenAndRefreshToken.length < 2) {
            return Result.getResult(AuthError.LOGIN_FAIL);
        }

        return Result.success(MapBuildUtil.builder()
                .data("token", tokenAndRefreshToken[0])
                .data("refresh_token", tokenAndRefreshToken[1])
                .get()
        );
    }


    @PostMapping("/refresh")
    public Result refresh(@RequestParam String refreshToken) {
        String[] tokenAndRefreshToken = authService.refresh(refreshToken);
        if (tokenAndRefreshToken == null || tokenAndRefreshToken.length < 2) {
            return Result.getResult(AuthError.REFRESH_TOKEN_IS_NOT_VALID);
        }

        return Result.success(MapBuildUtil.builder()
                .data("token", tokenAndRefreshToken[0])
                .data("refresh_token", tokenAndRefreshToken[1])
                .get()
        );
    }


}
