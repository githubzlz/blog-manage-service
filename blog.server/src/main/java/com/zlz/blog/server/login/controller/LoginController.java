package com.zlz.blog.server.login.controller;

import com.zlz.blog.common.entity.user.LoginUser;
import com.zlz.blog.common.enums.LoginTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.login.service.LoginUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-11-13 17:05
 * @description 登录逻辑
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private LoginUserService loginUserService;

    @ResponseBody
    @PostMapping("/login/getloginuser")
    public ResultSet<LoginUser> getLoginUser(HttpServletRequest request){
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) request.getSession().getAttribute("loginUser");
        } catch (Exception e){
            return ResultSet.error("查询失败");
        }
        return ResultSet.success("查询成功", loginUser);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResultSet loginPage(@RequestBody LoginUser loginUser){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(loginUser.getUsername(), loginUser.getPassword());
        try {
            //执行登录方法
            subject.login(token);
            ResultSet<LoginUser> byUsername = loginUserService.findByUsername(loginUser.getUsername());
            return ResultSet.success("登陆成功", byUsername.getEntity());
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            //用户名不存在
            return ResultSet.error("登陆失败,用户名或密码错误");
        }
    }

    @ResponseBody
    @RequestMapping("/login/register")
    public ResultSet registerUser(@RequestParam String username, @RequestParam String password) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        return loginUserService.registerUser(loginUser, LoginTypeEnum.PASSWORD.getCode());
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultSet registerUser(HttpServletRequest request){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultSet.success("退出登录成功");
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("/checkLogin")
    public ResultSet checkLogin(){
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            return ResultSet.success("登录");
        }
        return ResultSet.error("未登录");
    }
}