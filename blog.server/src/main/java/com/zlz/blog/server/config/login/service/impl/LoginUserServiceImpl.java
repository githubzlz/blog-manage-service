package com.zlz.blog.server.config.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.enums.LoginTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.EncryptionUtil;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.server.config.login.mapper.LoginUserMapper;
import com.zlz.blog.server.config.login.service.LoginUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-11-18 11:54
 * @description
 */
@Service
public class LoginUserServiceImpl implements LoginUserService {

    @Resource
    private LoginUserMapper userMapper;

    @Override
    public ResultSet<LoginUser> registerUser(LoginUser loginUser, Integer type) {
        Optional<LoginUser> loginUserOpt = Optional.of(loginUser);
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.getLoginTypeEnum(type);

        switch (loginTypeEnum){
            case PASSWORD:
                String username = loginUserOpt.get().getUsername();
                QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username);
                int count = userMapper.selectCount(queryWrapper);
                if(count != 0){
                    return ResultSet.error("用户名已经存在,请尝试新的用户名");
                }
                String salt = UUID.randomUUID().toString();
                String password = EncryptionUtil.md5Encrypt(loginUserOpt.get().getPassword(), salt);
                LoginUser user = new LoginUser();
                user.setSalt(salt);
                user.setUsername(username);
                user.setPassword(password);
                int insert = userMapper.insert(user);
                return SqlResultUtil.isOneRow(insert);
            case EMAIL_VERIFICATION_CODE:
                break;
            case PHONE_VERIFICATION_CODE:
                break;
            default:
                return ResultSet.error("注册失败");
        }
        return ResultSet.error("注册失败");
    }

    @Override
    public ResultSet<LoginUser> checkLoginUser(LoginUser loginUser, Integer type) {
        Optional<LoginUser> loginUserOpt = Optional.of(loginUser);
        String password = loginUserOpt.get().getPassword();
        String username = loginUserOpt.get().getUsername();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResultSet.error("缺少必要数据");
        }

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.getLoginTypeEnum(type);

        switch (loginTypeEnum){
            case PASSWORD:
                QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username);
                LoginUser userData = userMapper.selectOne(queryWrapper);
                if(userData.getPassword().equals(EncryptionUtil.md5Encrypt(password, userData.getSalt()))){
                    userData.setId(null);
                    userData.setPassword(null);
                    userData.setSalt(null);
                    return ResultSet.success("验证成功", userData);
                }
                break;
            case EMAIL_VERIFICATION_CODE:
                break;
            case PHONE_VERIFICATION_CODE:
                break;
            default:
                return ResultSet.error("登陆失败");
        }
        return ResultSet.error("登陆失败");
    }
}
