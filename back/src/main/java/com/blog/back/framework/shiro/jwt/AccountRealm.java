package com.blog.back.framework.shiro.jwt;


import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.back.POJO.entity.User;
import com.blog.back.service.UserService;
import com.blog.back.utils.JwtFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;


public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    public String getName() {
        return "myRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //授权器
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = principalCollection.getPrimaryPrincipal();
        if(!(principal instanceof AccountProfile primaryPrincipal))return null;
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取角色
        if("1111111111".equals(primaryPrincipal.getAccount())){
            authorizationInfo.addRole("admin");
        }else{
            authorizationInfo.addRole("user");
        }

        return authorizationInfo;
    }


    //认证器
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;

        String jwt = (String) jwtToken.getCredentials();
        Map<String, Claim> mes = JwtFactory.getMes(jwt);

        Integer id = mes.get("id").asInt();   //获取用户id
        User user = userService.getOne(new QueryWrapper<User>().eq("id",id).select("account","status","password","email","nickname"));

        //不存在或者被禁用的用户
        if(Objects.isNull(user)||0==user.getStatus())throw new UnknownAccountException("用户不存在或已被禁用！");

        AccountProfile accountProfile =new AccountProfile();

        BeanUtils.copyProperties(user,accountProfile);
        accountProfile.setId(id);

        return new SimpleAuthenticationInfo(accountProfile,jwtToken.getCredentials(),getName());
    }
}
