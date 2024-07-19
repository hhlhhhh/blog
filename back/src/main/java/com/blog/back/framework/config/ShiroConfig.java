package com.blog.back.framework.config;

import com.blog.back.framework.shiro.jwt.AccountRealm;
import com.blog.back.framework.shiro.jwt.JwtFilter;
import com.blog.back.framework.shiro.password.PasswordAccountRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Value("${blog.user.password.iteration}")
    private int passwordIteration;

    @Bean("jwt-filter")
    JwtFilter jwtFilter(){
        return new JwtFilter();
    }

    //认证realm
    @Bean
    public AccountRealm accountRealm(){
        return new AccountRealm();
    }

    //密码realm
    @Bean
    public PasswordAccountRealm passwordAccountRealm(){
        PasswordAccountRealm passwordAccountRealm = new PasswordAccountRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(passwordIteration);
        passwordAccountRealm.setCredentialsMatcher(matcher);
        return passwordAccountRealm;
    }

    //多realm认证管理
    @Bean("custom-modular-realm-auth")
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }


    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("custom-modular-realm-auth") ModularRealmAuthenticator authenticator,
                                                     AccountRealm accountRealm,
                                                     PasswordAccountRealm passwordAccountRealm,
                                                     @Qualifier("hhl-session-manager") SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(accountRealm);
        realms.add(passwordAccountRealm);
        securityManager.setAuthenticator(authenticator);
        securityManager.setRealms(realms);
        securityManager.setCacheManager(redisCacheManager());
        securityManager.setSessionManager(sessionManager);
        ThreadContext.bind(securityManager);
        return securityManager;
    }

    //cookie配置
    @Bean
    public SimpleCookie sessionIdCookie(){
        SimpleCookie cookie = new SimpleCookie("X-Token");
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        return cookie;
    }

    //sessionManager
    @Bean("hhl-session-manager")
    public DefaultWebSessionManager sessionManager(SimpleCookie sessionIdCookie,RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        //自定义cookie 中sessionId 的key
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdCookieEnabled(true);
        //使用redis 来持久化session 信息
        sessionManager.setSessionDAO(redisSessionDAO);
        //删除过期session
//        sessionManager.setDeleteInvalidSessions(true);
        //设置session 过期时间
//        sessionManager.setGlobalSessionTimeout(30*60*1000);
//        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }

    //redis连接管理，这个是shiro-redis配套的东西
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
//        redisManager.setPort(6379);
        return redisManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;

    }

    //redisCache管理
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //设置redis的缓存时间，单位是秒
        redisCacheManager.setExpire(20);
        return redisCacheManager;
    }


    //shiro过滤器
    @Bean
    public ShiroFilterFactoryBean  ShiroFilterFactoryBean(@Autowired@Qualifier("jwt-filter")JwtFilter filter,
                                                          DefaultWebSecurityManager securityManager) {
       ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
       factoryBean.setSecurityManager(securityManager);
       Map<String,Filter> map = new LinkedHashMap<>();
       map.put("login-filter",filter);
       factoryBean.setFilters(map);
       LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

       filterChainDefinitionMap.put("/user/login/account", "anon");
       filterChainDefinitionMap.put("/user/login/email", "anon");
       filterChainDefinitionMap.put("/user/email", "anon");
       filterChainDefinitionMap.put("/user/register", "anon");
       filterChainDefinitionMap.put("/user/verify", "anon");
       filterChainDefinitionMap.put("/media/*", "anon");

       filterChainDefinitionMap.put("/**", "login-filter");
       factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
       return factoryBean;
    }
}

