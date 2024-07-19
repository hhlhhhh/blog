package com.blog.back.framework.intercepter;

import com.auth0.jwt.interfaces.Claim;
import com.blog.back.POJO.entity.User;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.utils.JwtFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ReqInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String jwt =request.getHeader("Authorization");
        if(jwt == null)return true; //因为之前验证过了，没有jwt说明是不需要权限的请求
        Map<String, Claim> mes = JwtFactory.getMes(jwt);
        Integer id = mes.get("id").asInt();
        ReqInfoContext.getReqInfo().setUser(new User().setId(id)); //获取id
        return true;
    }
}
