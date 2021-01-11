package org.ks365.osmp.common.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.ks365.osmp.common.entity.FilterChainDefinition;
import org.ks365.osmp.common.entity.RedisProperties;
import org.ks365.osmp.common.entity.ShiroProperties;
import org.ks365.osmp.common.security.ShiroSessionListener;
import org.ks365.osmp.common.security.SystemAuthorizingRealm;
import org.ks365.osmp.common.security.session.CacheSessionDAO;
import org.ks365.osmp.common.security.session.SessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Shiro配置类
 *
 * @author tianslc
 * @date 2020/01/09
 */
@EnableCaching
@Configuration
public class ShiroConfig {

    private final ShiroProperties shiroProperties;
    private final RedisProperties redisProperties;

    public ShiroConfig(ShiroProperties shiroProperties, RedisProperties redisProperties) {
        this.shiroProperties = shiroProperties;
        this.redisProperties = redisProperties;
    }

    /**
     * 将自己的认证方式加入容器
     *
     * @return
     */
    @Bean
    public SystemAuthorizingRealm sysShiroRealm() {
        SystemAuthorizingRealm sysRealm = new SystemAuthorizingRealm();
        return sysRealm;
    }

    /**
     * 配置Shiro缓存
     *
     * @return
     */
/*    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:cache/ehcache-local.xml");
        return em;
    }*/

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }

    @Bean
    public CacheSessionDAO cacheSessionDAO(RedisCacheManager redisCacheManager) {
        CacheSessionDAO cacheSessionDAO = new CacheSessionDAO();
        cacheSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        cacheSessionDAO.setCacheManager(redisCacheManager);
        return cacheSessionDAO;
    }

    @Bean
    public SessionManager sessionManager(CacheSessionDAO cacheSessionDAO) {
        //将我们继承后重写的shiro session 注册
        SessionManager sessionManager = new SessionManager();
        List<SessionListener> listeners = new ArrayList<>();
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        //如果后续考虑多tomcat部署应用，可以使用shiro-redis开源插件来做session 的控制，或者nginx 的负载均衡
        sessionManager.setSessionDAO(cacheSessionDAO);
        return sessionManager;
    }

    /**
     * 配置session监听
     *
     * @return
     */
    @Bean
    public ShiroSessionListener sessionListener() {
        return new ShiroSessionListener();
    }

    /**
     * 配置权限管理器
     *
     * @param cacheManager
     * @return
     */
    @Bean
    public SecurityManager securityManager(RedisCacheManager cacheManager, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(sysShiroRealm());
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterMap = new LinkedHashMap<>();
        List<FilterChainDefinition> list = shiroProperties.getFilterChainDefinitionList();
        //读取配置文件中的过滤配置信息
        Collections.reverse(list);
        list.forEach(obj -> filterMap.put(obj.getKey(), obj.getValue()));
        //登录
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        //首页
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
        //将过滤配置注入
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * 开启aop注解支持
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