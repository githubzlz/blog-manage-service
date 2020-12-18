package com.zlz.blog.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/17 10:09
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new BlogUserInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/login", "/login/check", "/login/register", "/loginstatic/**","/send");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BlogUserInterceptor()).excludePathPatterns("/**");
    }
}
