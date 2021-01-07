package com.zlz.blog.server.config;

import com.zlz.blog.common.entity.user.LoginUser;
import com.zlz.blog.common.entity.user.SysPermission;
import com.zlz.blog.common.entity.user.SysRole;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.user.service.LoginUserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;


/**
 * created by zlz on 2020/10/22 17:20
 **/
public class ShiroRealm extends AuthorizingRealm{
    @Resource
    private LoginUserService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        LoginUser userInfo  = (LoginUser) principals.getPrimaryPrincipal();
        for(SysRole role:userInfo.getSysRoles()){
            authorizationInfo.addRole(role.getRole());
            for(SysPermission p:role.getSysPermissions()){
                authorizationInfo.addStringPermission(p.getEname());
            }
        }
        //CacheUntil.setCacheTree(authorizationInfo);
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token){
        //获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        char[] passwordC = (char[]) token.getCredentials();
        String credentials = new String(passwordC);

        // 检查登录信息，获取登录人
        ResultSet<LoginUser> loginUserResultSet = userInfoService.checkLoginUser(username, credentials, 0);
        LoginUser userInfo = loginUserResultSet.getEntity();
        if(userInfo == null){
            return null;
        }
        return new SimpleAuthenticationInfo(
                userInfo,
                credentials,
//                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                userInfo.getUsername()
        );
    }


}
