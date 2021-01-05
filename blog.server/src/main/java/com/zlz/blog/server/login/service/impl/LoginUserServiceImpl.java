package com.zlz.blog.server.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.user.LoginUser;
import com.zlz.blog.common.entity.user.SysRole;
import com.zlz.blog.common.enums.LoginTypeEnum;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.template.EmailRedisTemplate;
import com.zlz.blog.common.util.CheckCodeUtil;
import com.zlz.blog.common.util.EncryptionUtil;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.server.login.mapper.AuthenticationMapper;
import com.zlz.blog.server.login.mapper.LoginUserMapper;
import com.zlz.blog.server.login.service.LoginUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<Object, EmailRedisTemplate> emailRedisTemplate;


    @Override
    public ResultSet<LoginUser> registerUser(LoginUser loginUser, String type) {
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
    public ResultSet login(LoginUser loginUser) {

        if(null == loginUser){
            return ResultSet.error("登录失败,缺少用户信息");
        }

        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token;

        // 输入了用户名和密码就根据账号密码登录
        if(null != loginUser.getUsername() && null != loginUser.getPassword()){
            token = new UsernamePasswordToken(loginUser.getUsername(),
                    CheckCodeUtil.genePassWord(loginUser.getPassword()));
        // 使用邮箱登录
        }else if( null != loginUser.getEmail() || null != loginUser.getCheckCode()){
            token = new UsernamePasswordToken(loginUser.getEmail(),
                    CheckCodeUtil.geneEmailPassWord(loginUser.getCheckCode()));
        }else {
            return ResultSet.error("登陆失败，缺少重要参数");
        }

        //执行登录方法
        subject.login(token);
        LoginUser user = (LoginUser) subject.getPrincipal();
        LoginUser byUsername = null;
        if(user != null && StringUtils.isNotEmpty(user.getUsername())){
            byUsername = findByUsername(user.getUsername());
        }
        return ResultSet.success("登陆成功", byUsername);
    }

    @Override
    public ResultSet<LoginUser> checkLoginUser(LoginUser loginUser, Integer type) {

        if(loginUser == null || StringUtils.isEmpty(loginUser.getPassword())){
            return ResultSet.error("缺少必要数据");
        }
        String password = loginUser.getPassword();

        // 解析密码获取登陆类型
        LoginTypeEnum loginTypeEnum = CheckCodeUtil.getLoginType(password);
        // 通过密码获取真实登陆密码
        password = CheckCodeUtil.getRealPassWord(password);
        if(StringUtils.isEmpty(password)){
            throw new AuthenticationException("登录失败，用户名或密码错误");
        }
        if(null == loginTypeEnum){
            throw new AuthenticationException("登陆失败，未知的登陆方式");
        }

        switch (loginTypeEnum){
            case PASSWORD:
                // 查询用户是否存在
                if(StringUtils.isEmpty(loginUser.getUsername())){
                    throw new AuthenticationException("登录失败，用户名或密码错误");
                }
                LoginUser userData = findByUsername(loginUser.getUsername());

                // 检查密码是否正确
                if(userData == null || !userData.getPassword().equals(EncryptionUtil.md5Encrypt(password, userData.getSalt()))){
                    throw new AuthenticationException("登录失败，用户名或密码错误");
                }

                // 获权限信息，脱敏，并返回
                return ResultSet.success("验证成功", setAuthenticationInfo(userData));

            case EMAIL_VERIFICATION_CODE:

                // 检查邮箱是否已经注册
                LoginUser byEmail = findByEmail(loginUser.getUsername());
                if(null == byEmail){
                    throw new AuthenticationException("该邮箱尚未注册，请注册");
                }

                String title = "EMAIL_CHECK_CODE" + loginUser.getUsername();
                EmailRedisTemplate template = emailRedisTemplate.opsForValue().get(title);
                if(null == template
                        || StringUtils.isEmpty(template.getEmail())
                        || null == template.getUserId()) {
                    throw new AuthenticationException("登录失败，验证码错误");
                }

                if(!password.equals(template.getCheckCode())) {
                    throw new AuthenticationException("登录失败，验证码错误");
                }

                // 获权限信息并返回
                return ResultSet.success("验证成功", setAuthenticationInfo(byEmail));
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
    public LoginUser findByEmail(String email) {
        Optional<String> oName = Optional.of(email);

        // 查询用户
        QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", oName.get())
                .select("id", "email", "phone", "username");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public LoginUser findByUsername(String name) {
        Optional<String> oName = Optional.of(name);

        // 查询用户
        QueryWrapper<LoginUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", oName.get())
                .select("id", "email", "phone", "username", "salt", "password");
        LoginUser userData = userMapper.selectOne(queryWrapper);

        if(null != userData){
            // 获权限信息
            List<SysRole> roles = authenticationMapper.getAuthenticationInfo(userData.getId());
            userData.setSysRoles(roles);
        }
        return userData;
    }

    private LoginUser setAuthenticationInfo(LoginUser userData){
        // 获权限信息
        List<SysRole> roles = authenticationMapper.getAuthenticationInfo(userData.getId());
        LoginUser authenticationInfo = new LoginUser();
        authenticationInfo.setSysRoles(roles);
        authenticationInfo.setUsername(userData.getUsername());
        authenticationInfo.setEmail(userData.getEmail());
        return authenticationInfo;
    }
}
