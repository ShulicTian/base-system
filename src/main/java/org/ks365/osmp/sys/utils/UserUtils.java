package org.ks365.osmp.sys.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.ks365.osmp.common.entity.Principal;
import org.ks365.osmp.common.utils.CacheUtils;
import org.ks365.osmp.common.utils.SpringContextHolder;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.dao.AreaDao;
import org.ks365.osmp.sys.dao.MenuDao;
import org.ks365.osmp.sys.dao.RoleDao;
import org.ks365.osmp.sys.dao.UserDao;
import org.ks365.osmp.sys.dao.mapper.UserMapper;
import org.ks365.osmp.sys.entity.AreaEntity;
import org.ks365.osmp.sys.entity.MenuEntity;
import org.ks365.osmp.sys.entity.RoleEntity;
import org.ks365.osmp.sys.entity.UserEntity;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户相关工具类
 *
 * @author tianslc
 * @date 2020/01/13
 */
public class UserUtils {

    public static final String USER_CACHE = "userCache";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AUTH_INFO = "authInfo";
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "loginName_";
    private static UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
    private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);

    public static boolean isAdmin() {
        return getCurrentUser() != null && getCurrentUser().isAdmin();
    }

    public static UserEntity getCurrentUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            UserEntity user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return null;
        }
        return null;
    }

    public static boolean hasRoleById(Integer id) {
        long exist = Objects.requireNonNull(getCurrentUser()).getRoleList().stream().filter(role -> role.getId().equals(id)).count();
        return exist > 0;
    }

    public static boolean hasRoleByEnName(String enName) {
        long exist = Objects.requireNonNull(getCurrentUser()).getRoleList().stream().filter(role -> role.getEnName().equals(enName)).count();
        return exist > 0;
    }

    public static boolean hasRoleByName(String name) {
        long exist = Objects.requireNonNull(getCurrentUser()).getRoleList().stream().filter(role -> role.getName().equals(name)).count();
        return exist > 0;
    }

    public static List<String> getCurrentUserRoleNames() {
        return Objects.requireNonNull(getCurrentUser()).getRoleList().stream().map(RoleEntity::getName).collect(Collectors.toList());
    }

    public static Integer getCurrentUserId() {
        UserEntity user = getCurrentUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException | InvalidSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {

        }
        return null;
    }

    // ============== UserEntity Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        Objects.requireNonNull(getSession()).setAttribute(key, value);
    }

    public static void removeCache(String key) {
        Objects.requireNonNull(getSession()).removeAttribute(key);
    }

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static UserEntity get(Integer id) {
        UserEntity user = JSON.parseObject(CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id) + "", UserEntity.class);
        if (user == null) {
            Optional<UserEntity> optional = userDao.findById(id);
            if (optional.isPresent()) {
                user = optional.get();
            }
            if (user == null) {
                return null;
            }
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), JSON.toJSONString(user));
        }
        return user;
    }

    public static UserEntity getByUserName(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return null;
        }
        UserEntity user = JSON.parseObject(CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName) + "", UserEntity.class);
        if (user == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(loginName);
            Example<UserEntity> example = Example.of(userEntity);
            Optional<UserEntity> optional = userDao.findOne(example);
            if (optional.isPresent()) {
                user = optional.get();
            }
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName, JSON.toJSONString(user));
        }
        return user;
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<RoleEntity> getRoleList() {
        @SuppressWarnings("unchecked")
        List<RoleEntity> roleList = (List<RoleEntity>) getCache(CACHE_ROLE_LIST);
        if (roleList == null) {
            UserEntity user = getCurrentUser();
            if (user != null) {
                if (user.isAdmin()) {
                    roleList = roleDao.findAll();
                } else {
                    roleList = user.getRoleList();
                }
                putCache(CACHE_ROLE_LIST, roleList);
            }
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<MenuEntity> getMenuList() {
        List<MenuEntity> menuList = (List<MenuEntity>) getCache(CACHE_MENU_LIST);
        if (menuList == null) {
            UserEntity user = getCurrentUser();
            if (user != null) {
                if (user.isAdmin()) {
                    menuList = menuDao.findAll();
                } else {
                    List<MenuEntity> finalMenuList = Lists.newArrayList();
                    List<RoleEntity> roleList = user.getRoleList();
                    roleList.forEach(role -> finalMenuList.addAll(role.getMenuList()));
                    menuList = finalMenuList;
                    menuList.add(new MenuEntity(0, 0));
                }
                putCache(CACHE_MENU_LIST, menuList);
            }
        }
        return menuList;
    }

    /**
     * 获取当前用户授权的区域
     *
     * @return
     */
    public static List<AreaEntity> getAreaList() {
        @SuppressWarnings("unchecked")
        List<AreaEntity> areaList = (List<AreaEntity>) getCache(CACHE_AREA_LIST);
        if (areaList == null) {
            areaList = areaDao.findAll();
            putCache(CACHE_AREA_LIST, areaList);
        }
        return areaList;
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_AUTH_INFO);
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_MENU_LIST);
        removeCache(CACHE_AREA_LIST);
        UserUtils.clearCache(getCurrentUser());
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(UserEntity user) {
        if (user != null) {
            CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
            CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getUsername());
        }
    }

    public static boolean isExists(UserEntity userEntity) {
        Example<UserEntity> example = Example.of(userEntity);
        return userDao.exists(example);
    }

    public static boolean isExists(String mobile) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(mobile);
        return isExists(userEntity);
    }

    public static UserEntity save(UserEntity userEntity) {
        return userDao.saveAndFlush(userEntity);
    }

    public static UserEntity getByMobile(String mobile) {
        return userMapper.getByMobile(mobile);
    }

    public static UserEntity get(UserEntity userEntity) {
        return userDao.findOne(Example.of(userEntity)).orElse(null);
    }

}
