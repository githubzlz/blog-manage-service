package com.zlz.blog.server.email.service;

import com.zlz.blog.common.response.ResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-11-20 16:18
 * @description
 */
@RestController
public class EmailController {

    @Resource
    private SendEmailService sendEmailService;


    @PostMapping("/send")
    public ResultSet sendEmail(@RequestParam String title, @RequestParam String content){
        sendEmailService.sendSimpleMail("sdbz_zlz@163.com", title, content);
        return ResultSet.success("发送成功");
    }
}
