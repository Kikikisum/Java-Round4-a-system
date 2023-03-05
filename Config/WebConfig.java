package com.server.Config;

import com.server.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private TokenInterceptor tokenInterceptor;
//
//    @Value("${file.upload-path}")
//    private String filePath;//静态资源设置
//    final String[] notInterceptorPaths = {"/login","/captcha","/register"};//过滤器，不会对相关接口检测token
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/sys/**")
//                .excludePathPatterns(notInterceptorPaths);
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("file:"+filePath);
//        System.out.println("静态资源获取");
//    }
}
