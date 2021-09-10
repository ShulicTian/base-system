package org.ks365.osmp.common.security;

/**
 * @author tianslc
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {
    private static final long serialVersionUID = -2830508458391062179L;
    private String loginType = "0";

    public UsernamePasswordToken(String username, char[] password,
                                 boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public UsernamePasswordToken(String username, String password,
                                 boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public UsernamePasswordToken(String username,
                                 boolean rememberMe, String loginType) {
        super(username, "", rememberMe);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

}
