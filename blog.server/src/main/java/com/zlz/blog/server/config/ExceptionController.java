package com.zlz.blog.server.config;

import com.zlz.blog.common.exception.BlogException;
import com.zlz.blog.common.response.ResultSet;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-06 09:17
 * @description 全局统一异常处理
 */
@ControllerAdvice
@RestController
@Log4j2
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    public ResultSet exceptionHandler(Exception e) {
        log.error("发生异常:", e);
        if(e instanceof BlogException){
            return ResultSet.error(e.getMessage());
        }else if(e instanceof UnauthorizedException){
            String message = e.getMessage();
            boolean permission = message.contains("permission");
            boolean role = message.contains("role");
            String substring = message.substring(message.indexOf("[")+1, message.indexOf("]"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("对不起，该操作需要");
            if(role){
                stringBuilder.append(" [").append(substring).append("] 角色。");
            }else if(permission){
                stringBuilder.append(" [").append(substring).append("] 权限。");
            }else {
                stringBuilder.append("更高的权限。");
            }
            return ResultSet.unauthorizedError(stringBuilder.toString());
        }else if (e instanceof UnauthenticatedException) {
            return ResultSet.loginError("登陆失败，用户名或密码错误");
        }else {
            return ResultSet.error(e.getMessage());
        }
    }
}
