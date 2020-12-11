package com.zlz.blog.server.config.login.controller;

import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.enums.LoginTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.config.login.service.LoginUserService;
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

    @RequestMapping("/login")
    public String loginPage(Model model, HttpServletRequest request){
        if(request.getQueryString() == null){
            model.addAttribute("error", "null");
        }else{
            model.addAttribute("error", "error");
        }
        String formAction = "/login/check";
        model.addAttribute("formAction",formAction);
        return "login";
    }

    @ResponseBody
    @RequestMapping("/login/check")
    public ResultSet checkLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpServletResponse response,
                                HttpServletRequest request) throws IOException {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        ResultSet<LoginUser> loginUserResultSet =
                loginUserService.checkLoginUser(loginUser, LoginTypeEnum.PASSWORD.getCode());
        if(!ResultSet.isSuccess(loginUserResultSet)){
            response.sendRedirect("/login?error");
        }else {
            request.getSession().setAttribute("loginUser", loginUserResultSet.getEntity());
        }
        response.sendRedirect("/");
        return ResultSet.success("登录成功");
    }

    @ResponseBody
    @RequestMapping("/login/register")
    public ResultSet registerUser(@RequestParam String username,
                                  @RequestParam String password,
                                  HttpServletRequest request) throws IOException {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        ResultSet<LoginUser> loginUserResultSet1 =
                loginUserService.registerUser(loginUser, LoginTypeEnum.PASSWORD.getCode());

        ResultSet<LoginUser> loginUserResultSet =
                loginUserService.checkLoginUser(loginUser, LoginTypeEnum.PASSWORD.getCode());
        request.getSession().setAttribute("loginUser", loginUser);
        return ResultSet.success("注册成功");
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultSet registerUser(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");
        return ResultSet.success("退出登录成功");
    }
}
