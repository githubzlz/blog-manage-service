package com.zlz.blog.server.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.user.LoginUser;
import com.zlz.blog.common.entity.user.SysRole;
import com.zlz.blog.common.enums.LoginTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.EncryptionUtil;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.server.login.mapper.AuthenticationMapper;
import com.zlz.blog.server.login.mapper.LoginUserMapper;
import com.zlz.blog.server.login.service.LoginUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
    @Resource
    private AuthenticationMapper authenticationMapper;

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

                // 查询用户是否存在
                QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", username);
                LoginUser userData = userMapper.selectOne(queryWrapper);

                // 检查密码是否正确
                if(userData == null || !userData.getPassword().equals(EncryptionUtil.md5Encrypt(password, userData.getSalt()))){
                    return ResultSet.error("登陆失败");
                }

                // 获权限信息
                List<SysRole> roles = authenticationMapper.getAuthenticationInfo(userData.getId());
                LoginUser authenticationInfo = new LoginUser();
                authenticationInfo.setSysRoles(roles);
                authenticationInfo.setUsername(userData.getUsername());
                authenticationInfo.setEmail(userData.getEmail());
                return ResultSet.success("验证成功", authenticationInfo);

            case EMAIL_VERIFICATION_CODE:
                break;
            case PHONE_VERIFICATION_CODE:
                break;
            default:
                return ResultSet.error("登陆失败");
        }
        return ResultSet.error("登陆失败");
    }

    @Override
    public ResultSet<LoginUser> checkLoginUser(String username, String password, Integer type) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        return checkLoginUser(loginUser, type);
    }

    @Override
    public ResultSet<LoginUser> findByUsername(String name) {
        Optional<String> oName = Optional.of(name);

        // 查询用户
        QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", oName.get())
                .select("id", "email", "phone", "username");
        LoginUser userData = userMapper.selectOne(queryWrapper);



        if(null != userData){
            // 获权限信息
            List<SysRole> roles = authenticationMapper.getAuthenticationInfo(userData.getId());
            userData.setSysRoles(roles);
            return ResultSet.success("查询用户信息成功", userData);
        }
        return ResultSet.success("查询用户信息失败");
    }
}
