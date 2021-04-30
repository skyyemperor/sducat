package com.sducat.framework.filter;

import com.sducat.common.util.ServletUtil;
import com.sducat.common.util.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by skyyemperor on 2021-02-16 11:18
 * Description : 将请求记录写入日志
 */
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Map<String, String> allParameters = ServletUtil.getAllParameters();
        StringBuilder param = new StringBuilder();
        param.append("Content-Type").append(":").append(request.getHeader("Content-Type")).append("\n");
        param.append("TOKEN").append(":").append(request.getHeader("TOKEN")).append("\n");
        param.append("STAMP").append(":").append(request.getHeader("STAMP")).append("\n");
        param.append("SIGN").append(":").append(request.getHeader("SIGN")).append("\n");
        for (Map.Entry<String, String> entry : allParameters.entrySet()) {
            param.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        log.info(request.getRequestURI() + " " + IpUtils.getIpAddr(request) + "\n" + param.toString());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
