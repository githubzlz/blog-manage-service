package com.zlz.blog.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 公共配置类
 * created by zlz on 2020/12/30 10:10
 **/
@Configuration
public class CommonConfig {

    @Value("${spring.shiro.login.path}")
    public String shiroLoginPath;

}
