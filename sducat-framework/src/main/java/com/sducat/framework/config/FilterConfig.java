package com.sducat.framework.config;

import com.sducat.framework.filter.LogFilter;
import com.sducat.framework.filter.SignFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by skyyemperor on 2021-01-30 14:59
 * Description :
 */
@Configuration
public class FilterConfig {

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public FilterRegistrationBean logFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logFilter");
        registration.setOrder(1);
        return registration;
    }

//    @Bean
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public FilterRegistrationBean signFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new SignFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("signFilter");
//        registration.setOrder(2);
//        return registration;
//    }

}
