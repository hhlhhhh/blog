package com.blog.back.framework.shiro.jwt;


import com.alibaba.fastjson.JSON;
import com.blog.back.framework.resp.Result;
import com.blog.back.framework.resp.ResultState;
import com.blog.back.utils.JwtFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class JwtFilter extends AuthenticatingFilter {

    public void writeFail(ServletResponse response){
        Result result = Result.fail(null, ResultState.AUTHENTICATIONFAIL,"认证失败！");
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //在下面那个类执行executeLogin，执行后executeLogin会调用这个方法作为一个登录凭证，也就是一个token
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt =request.getHeader("Authorization");
        return new JwtToken(jwt);
    }

    //访问拒绝的时候调用，反正就是如果这个接口需要访问权限，它就会拒绝你的访问，你在这里面进行登录操作
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt =request.getHeader("Authorization");
        try{
            if(StringUtils.isBlank(jwt)){
                writeFail(servletResponse);
                return false;
            }else{
                if(!JwtFactory.verify(jwt)){
                    throw new ExpiredCredentialsException("token已失效，请重新登录");
                }
            }
        }catch (ExpiredCredentialsException e){
            writeFail(servletResponse);
            return false;
        }

        //执行登录
        return executeLogin(servletRequest,servletResponse);
    }


    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Throwable throwable = e.getCause()==null?e:e.getCause();
        PrintWriter writer = null;
        Result res = Result.fail(throwable.getMessage());
        try {
            writer = response.getWriter();
            writer.print(res);
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            if(writer != null){
                writer.close();
            }
        }
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
