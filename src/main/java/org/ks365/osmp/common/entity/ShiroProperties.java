package org.ks365.osmp.common.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 加载Shiro配置文件信息
 *
 * @author tianslc
 * @date 2020/01/09
 */

@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String loginUrl;
    private String unauthorizedUrl;
    private String successUrl;
    private List<FilterChainDefinition> filterChainDefinitionList;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public List<FilterChainDefinition> getFilterChainDefinitionList() {
        return filterChainDefinitionList;
    }

    public void setFilterChainDefinitionList(List<FilterChainDefinition> filterChainDefinitionList) {
        this.filterChainDefinitionList = filterChainDefinitionList;
    }
}
