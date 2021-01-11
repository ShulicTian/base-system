package org.ks365.osmp.common.filters;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;

public class AuthFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        String[] perms = (String[]) o;
        if (Arrays.asList(perms).contains("user")) {
            servletResponse.getWriter().write("TEst");
        }
        return false;
    }
}
