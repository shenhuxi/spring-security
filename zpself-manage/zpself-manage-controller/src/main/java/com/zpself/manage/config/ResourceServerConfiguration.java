package com.zpself.manage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源访问的切面
 * Created by zengpeng on 2019/3/25
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
       //for (String url : dxApiGateWateProperties.getUrls().getNoAuth()) {
            registry.antMatchers("/hi").permitAll();//url过滤掉权限的路径
       // }
        registry.anyRequest()
                .access("@permissionService.hasPermission(request,authentication)");
    }
}
