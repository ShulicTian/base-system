package org.ks365.osmp.common.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.ks365.osmp.common.entity.Principal;
import org.ks365.osmp.common.interceptor.Servlets;
import org.ks365.osmp.common.utils.Encodes;
import org.ks365.osmp.common.utils.LogUtils;
import org.ks365.osmp.sys.dao.UserDao;
import org.ks365.osmp.sys.entity.MenuEntity;
import org.ks365.osmp.sys.entity.RoleEntity;
import org.ks365.osmp.sys.entity.UserEntity;
import org.ks365.osmp.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shiro认证及授权
 *
 * @author tianslc
 * @date 2020/01/09
 */
@Component
public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    /**
     * 认证授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        UserEntity user = UserUtils.getByUserName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<MenuEntity> list = UserUtils.getMenuList();
            for (MenuEntity menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            info.addRoles(user.getRoleList().stream().map(RoleEntity::getEnName).collect(Collectors.toList()));
            for (RoleEntity role : user.getRoleList()) {
                info.addRole(role.getEnName());
                // 添加菜单权限
                if (role.getMenuList() != null && role.getMenuList().size() > 0) {
                    List<String> perms = role.getMenuList().stream().
                            filter(menuEntity -> StringUtils.isNotEmpty(menuEntity.getPermission())).
                            map(MenuEntity::getPermission).collect(Collectors.toList());
                    info.addStringPermissions(perms);
                }
            }
            // 更新登录IP和时间
            userDao.save(user);
            // 记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        }
        return null;
    }

    /**
     * 获取权限授权信息，如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用
     */
    @Override
    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            return null;
        }
        AuthorizationInfo info = null;
        info = (AuthorizationInfo) UserUtils.getCache(UserUtils.CACHE_AUTH_INFO);
        if (info == null) {
            info = doGetAuthorizationInfo(principals);
            if (info != null) {
                UserUtils.putCache(UserUtils.CACHE_AUTH_INFO, info);
            }
        }
        return info;
    }

    /**
     * 认证用户信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        // 校验用户名密码
        UserEntity user = UserUtils.getByUserName(token.getUsername());
        if (user != null) {
            if ("1".equals(user.getFlag())) {
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }
            byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(new Principal(user),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        }
        return null;
    }

    /**
     * 设置密码解析方式
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        CredentialsMatcher matcher = new CredentialsMatcher("SHA-1");
        matcher.setHashIterations(1024);
        setCredentialsMatcher(matcher);
    }


    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     *
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }
}
