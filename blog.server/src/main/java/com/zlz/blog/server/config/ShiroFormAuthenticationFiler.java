package com.zlz.blog.server.config;

import cn.hutool.json.JSONObject;
import com.zlz.blog.common.response.ResultSet;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * created by zlz on 2020/12/29 11:35
 **/
public class ShiroFormAuthenticationFiler extends AuthenticationFilter {

//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/json; charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        ResultSet resultSet = ResultSet.loginError("登陆失败");
//        JSONObject jsonObject = new JSONObject(resultSet);
//        writer.write(jsonObject.toString());
//        return false;
//    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }
}
