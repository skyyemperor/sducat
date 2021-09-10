package com.sducat.admin.controller;

import com.alibaba.fastjson.JSON;
import com.sducat.common.core.result.error.AuthError;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.MapBuildUtil;
import com.sducat.common.util.OkHttpUtil;
import com.sducat.framework.service.AuthService;
import com.sducat.system.data.po.Cat;
import com.sducat.system.data.po.Comment;
import com.sducat.system.data.po.NewCat;
import com.sducat.system.data.po.ScienceModule;
import com.sducat.system.mapper.CatMapper;
import com.sducat.system.mapper.CommentMapper;
import com.sducat.system.mapper.NewCatMapper;
import com.sducat.system.mapper.ScienceModuleMapper;
import com.sducat.system.service.CatService;
import com.sducat.system.service.CommentService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by skyyemperor on 2021-01-29 0:09
 * Description :
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/test")
    public String test() {
        return "true";
    }

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
