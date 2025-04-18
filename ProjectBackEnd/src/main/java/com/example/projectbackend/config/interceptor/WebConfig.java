package com.example.projectbackend.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置白名单
        List<String> whileList = new ArrayList<>();
        whileList.add("/user/login");
        whileList.add("/user/register");
        whileList.add("/resource/**");
        whileList.add("/files/download");
        whileList.add("/files/downloadChunk");
        //配置黑名单
        List<String> blackList = new ArrayList<>();
        blackList.add("/**");
        registry.addInterceptor(loginCheckInterceptor)
                //拦截所有发往有关用户资源的请求
                .addPathPatterns(blackList)
                //不拦截注册和登录页面资源
                .excludePathPatterns(whileList);
    }
}
