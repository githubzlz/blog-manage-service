package com.zlz.blog.server.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * created by zlz on 2020/10/22 17:15
 **/

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /**
            anon: 无需认证就可以访问
            authc: 必须认证了才能访问
            user: 必须拥有 记住我 功能才能用
            perms: 拥有对某个资源的权限才能访问
            role： 拥有某个角色权限才能访问
         */
        //拦截器.
        Map<String,String> filterMap = new LinkedHashMap<>();
//        filterMap.put("/user/add","perms[user:add]");
//        filterMap.put("/user/update","perms[user:update]");
        filterMap.put("/login","anon");
        filterMap.put("/**", "authc");
        //设置登录的请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        Map<String, Filter> filter = new LinkedHashMap<>(1);
        filter.put("authc", new ShiroFormAuthenticationFiler());
        shiroFilterFactoryBean.setFilters(filter);

        return shiroFilterFactoryBean;
    }
//    /**
//     * 凭证匹配器
//     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
//     * ）
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }
//
//    @Bean
//    public ShiroRealm myShiroRealm(){
//        ShiroRealm myShiroRealm = new ShiroRealm();
//        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return myShiroRealm;
//    }
//
//
//    @Bean
//    public SecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        return securityManager;
//    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
//
//    @Bean(name="simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver
//    createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "/base/databaseError");//数据库异常处理
//        mappings.setProperty("UnauthorizedException","/base/error");
//        r.setExceptionMappings(mappings);  // None by default
//        //r.setDefaultErrorView("error");    // No default
//        r.setDefaultErrorView("/base/login");    // 访问错误，直接访问登录页面
//        r.setExceptionAttribute("ex");     // Default is "exception"
//        //r.setWarnLogCategory("example.MvcLogger");     // No default
//        return r;
//    }


    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("shiroRealm") ShiroRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联 UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建 realm 对象 ， 需要自定义类1 ,@Bean被Spring托管了
    @Bean
    public ShiroRealm shiroRealm(){
        return new ShiroRealm();
    }


}
