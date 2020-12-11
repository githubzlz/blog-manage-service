package com.zlz.blog.common.enums;

import com.zlz.blog.common.exception.BlogException;

public enum LoginTypeEnum {

    /**
     * 账号密码登录
     */
    PASSWORD(0, "账号密码登录"),

    /**
     * 邮箱验证码登录
     */
    EMAIL_VERIFICATION_CODE(1, "邮箱验证码登录"),

    /**
     * 手机验证码登录
     */
    PHONE_VERIFICATION_CODE(2, "手机验证码登录");

    private Integer code;
    private String name;

    LoginTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 通过code获取枚举
     * @param code
     * @return
     */
    public static LoginTypeEnum getLoginTypeEnum(Integer code){
        if(null != code){
            for(LoginTypeEnum typeEnum : LoginTypeEnum.values()){
                if(typeEnum.getCode().equals(code)){
                    return typeEnum;
                }
            }
        }
        throw new BlogException("获取枚举异常，错误的code");
    }
}
