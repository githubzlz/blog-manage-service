package com.zlz.blog.server.config.login.service;

import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.response.ResultSet;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-11-18 11:53
 * @description
 */
public interface LoginUserService {

    /**
     * 用户注册
     * @param loginUser
     * @param type
     * @return
     */
    ResultSet<LoginUser> registerUser(LoginUser loginUser, Integer type);

    /**
     * 检查用户信息
     * @param loginUser
     * @param type 登陆方式
     * @return
     */
    ResultSet<LoginUser> checkLoginUser(LoginUser loginUser, Integer type);
}
