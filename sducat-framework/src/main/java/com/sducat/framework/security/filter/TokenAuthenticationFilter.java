package com.sducat.framework.security.filter;

import com.sducat.common.core.data.dto.LoginUser;
import com.sducat.common.util.SecurityUtil;
import com.sducat.common.util.StringUtil;
import com.sducat.framework.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * token过滤器 验证token有效性
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtil.isNotNull(loginUser) && StringUtil.isNull(SecurityUtil.getAuthentication())) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        RequestParameterWrapper wrapper = new RequestParameterWrapper(request);
        if (StringUtil.isNotNull(loginUser)) {
            //将userId以参数`_uid_`的形式添加到请求中
            HashMap<String, Object> param = new HashMap<>();
            param.put("_uid_", loginUser.getUser().getUserId());
            wrapper.addParameters(param);
        }
        chain.doFilter(wrapper, response);
    }
}
