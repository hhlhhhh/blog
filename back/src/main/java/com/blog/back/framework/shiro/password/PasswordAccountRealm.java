package com.blog.back.framework.shiro.password;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.back.POJO.entity.User;
import com.blog.back.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


public class PasswordAccountRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    public String getName() {
        return "account-password-realm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //认证器
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String account =  token.getUsername();

        User user = userService.getOne(new QueryWrapper<User>()
                .eq("status",1)
                .eq("account",account)
                .select("id","status","password","email"));

        if(Objects.isNull(user)||0==user.getStatus())throw new UnknownAccountException("用户不存在或已被禁用！");

        PasswordAccountProfile passwordAccountProfile =new PasswordAccountProfile();

        BeanUtils.copyProperties(user,passwordAccountProfile);
        passwordAccountProfile.setAccount(account);

        return new SimpleAuthenticationInfo(passwordAccountProfile,user.getPassword(), ByteSource.Util.bytes(account),getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = principalCollection.getPrimaryPrincipal();
        if(!(principal instanceof PasswordAccountProfile primaryPrincipal))return null;
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取角色
        if("1111111111".equals(primaryPrincipal.getAccount())){
            authorizationInfo.addRole("admin");
        }else{
            authorizationInfo.addRole("user");
        }

        return authorizationInfo;
    }
}
