package com.gpchen.blog.config;

import com.gpchen.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;

    //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
    //本地测试 端口不一致 也算跨域
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    public void addInterceptors(InterceptorRegistry registry){
        //Interceptors for login interceptor, when there are pages needed to be login, this will triggered
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/comments/create/change")//comment only when login
                .addPathPatterns("/articles/publish");
    }
}
