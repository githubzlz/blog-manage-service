package com.zlz.blog.server.config;

import com.zlz.blog.common.entity.common.LoginUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/17 10:13
 */
public class BlogUserInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(BlogUserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if(null == loginUser || StringUtils.isEmpty(loginUser.getUsername())){
            response.sendRedirect("/login");
        }
        return true;
    }
}
