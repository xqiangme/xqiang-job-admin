package com.xqiang.job.admin.core.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 配置
 *
 * @author mengq
 */
@Slf4j
@Configuration
public class JobAdminShiroConfig {

    public static final int EXPIRE = 3600;
    public static final long SESSION_EXPIRATION_TIME = EXPIRE * 1000;
    public static final String COOKIE_PREFIX = "job.admin.cookie.";

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean createShiroFilter(SecurityManager securityManager) {
        log.debug("[ JobAdminShiroConfig ] >> create start .");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //登录地址
        shiroFilterFactoryBean.setLoginUrl("/job-admin/login");
        //无权限跳转地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/job-admin/404");
        //成功后跳转地址
        shiroFilterFactoryBean.setSuccessUrl("/job-admin/index");

        //自定义认证过滤器
        Map<String, Filter> filterMap = new HashMap<>(16);
        //权限认证过滤器
        filterMap.put("authc", new JobAdminPermissionFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/job-admin/login", "anon");
        filterChainDefinitionMap.put("/job-admin/404", "anon");
        filterChainDefinitionMap.put("/job-admin/login/getVerifyCode", "anon");
        //登录
        filterChainDefinitionMap.put("/job-admin/login/in", "anon");
        //登出
        filterChainDefinitionMap.put("/job-admin/login/out", "anon");
        //页面跳转链接不拦截
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/job-admin/index", "anon");
        filterChainDefinitionMap.put("/job-admin/page/**", "anon");
        //静态页面文件
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/job-admin/static/**", "anon");
        filterChainDefinitionMap.put("/monitor/**", "anon");
        filterChainDefinitionMap.put("/test/**", "anon");
        //拦截其它所有请求
        filterChainDefinitionMap.put("/job-admin/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        log.debug("[ JobAdminShiroConfig ] >> create end .");
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(JobAdminAuthRealm jobAdminAuthRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //身份认证realm.
        securityManager.setRealm(jobAdminAuthRealm);
        return securityManager;
    }


    /**
     * cookie的名称  原因就是会跟原来的session的id值重复的
     *
     * @return
     */
    @Bean
    public SimpleCookie simpleCookie() {
        return new SimpleCookie(COOKIE_PREFIX);
    }


    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
