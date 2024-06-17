package com.example.springtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String ROOT_PATH=System.getProperty("user.dir")+ File.separator+"resource";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /resource/**是静态映射， file:/root/resource/是文件在服务器的路径
        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:"+ROOT_PATH+"/");
    }
}
