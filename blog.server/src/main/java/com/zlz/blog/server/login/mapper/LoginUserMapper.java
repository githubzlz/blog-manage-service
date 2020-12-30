package com.zlz.blog.server.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz.blog.common.entity.user.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import sun.plugin2.message.GetAuthenticationMessage;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-11-18 11:53
 * @description
 */
@Mapper
public interface LoginUserMapper extends BaseMapper<LoginUser> {
}
