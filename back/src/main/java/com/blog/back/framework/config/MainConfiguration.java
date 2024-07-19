package com.blog.back.framework.config;

import com.blog.back.framework.intercepter.ReqInfoInterceptor;
import org.apache.catalina.connector.Connector;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class MainConfiguration implements WebMvcConfigurer {

    @Resource
    RedisTemplate<Object,Object> template;

    @Autowired
    ShiroFilterFactoryBean shiroFilterFactoryBean;

    @PostConstruct
    void init(){
        RedisTemplateCache.setTemplate(template);
    }


//      传数组
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedPathChars", "|{}[]");
                connector.setProperty("relaxedQueryChars", "|{}[]");
            }
        });
        return factory;
    }

    @Bean
    public ReqInfoInterceptor reqInfoInterceptor(){
        return new ReqInfoInterceptor();
    }

    //获取怕不需要拦截器的路径
    public List<String> getExcludeChainPathPatterns(){
        Map<String, String> chainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        List<String> excludePathPatterns = new ArrayList<>();
        chainDefinitionMap.forEach((key,value)->{
            if(!value.equals("login-filter"))excludePathPatterns.add(key);
        });
        return excludePathPatterns;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(reqInfoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(getExcludeChainPathPatterns());
    }

}
