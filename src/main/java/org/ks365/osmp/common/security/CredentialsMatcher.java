package org.ks365.osmp.common.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.context.annotation.Configuration;

/**
 * @author tianslc
 */
@Configuration
public class CredentialsMatcher extends HashedCredentialsMatcher {
    private final String NOT_AUTH = "1";

    public CredentialsMatcher() {
    }

    public CredentialsMatcher(String hashAlgorithmName) {
        super(hashAlgorithmName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 跳过认证
        if (NOT_AUTH.equals(token.getLoginType())) {
            return true;
        }
        return super.doCredentialsMatch(token, info);
    }
}
