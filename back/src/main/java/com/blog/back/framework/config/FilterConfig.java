package com.blog.back.framework.config;

import com.blog.back.framework.shiro.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtFilter myFilter;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myFilter);
        registrationBean.setName("jwt-filter");
        registrationBean.setEnabled(false);
        return registrationBean;
    }

}