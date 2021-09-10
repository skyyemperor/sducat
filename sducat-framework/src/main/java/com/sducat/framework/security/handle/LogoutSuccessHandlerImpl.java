package com.sducat.framework.security.handle;

import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.core.result.Result;
import com.sducat.common.util.ServletUtil;
import com.sducat.common.util.StringUtil;
import com.sducat.framework.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtil.isNotNull(loginUser)) {
//            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.logout(loginUser.getToken());
        }
        ServletUtil.responseResult(response, Result.success());
    }
}
