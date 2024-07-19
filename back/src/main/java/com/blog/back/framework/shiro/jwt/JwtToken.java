package com.blog.back.framework.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {  //相当于用户名
        return token;
    }

    @Override
    public Object getCredentials() {   //相当于密码
        return token;
    }
}
