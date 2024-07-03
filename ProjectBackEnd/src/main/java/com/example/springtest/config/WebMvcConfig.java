package com.example.springtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

// 配置静态资源映射
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final String ROOT_PATH=System.getProperty("user.dir")+ File.separator+"resource";
    private static final String IMAGES_PATH=System.getProperty("user.dir")+ File.separator+"resource"+ File.separator+"images";
    private static final String VIDEOS_PATH=System.getProperty("user.dir")+ File.separator+"resource"+ File.separator+"videos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /resource/**是静态映射， file:/root/resource/是文件在服务器的路径
        registry.addResourceHandler("/resource/images/**")
                .addResourceLocations("file:"+IMAGES_PATH+"/");

        registry.addResourceHandler("/resource/videos/**")
                .addResourceLocations("file:"+VIDEOS_PATH+"/");
    }
}
