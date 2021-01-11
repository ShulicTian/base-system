/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.sys.dao.LogDao;
import org.ks365.osmp.sys.dao.MenuDao;
import org.ks365.osmp.sys.entity.LogEntity;
import org.ks365.osmp.sys.entity.MenuEntity;
import org.ks365.osmp.sys.entity.UserEntity;
import org.ks365.osmp.sys.utils.UserUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 日志工具类
 *
 * @author tianslc
 */
public class LogUtils {

    public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
    public static final String IS_SHOW = "1";
    public static final String SEPARATOR = ",";

    private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title);
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
        UserEntity user = UserUtils.getCurrentUser();
        if (user != null && user.getId() != null) {
            LogEntity logEntity = new LogEntity();
            logEntity.setTitle(title);
            logEntity.setType(ex == null ? LogEntity.TYPE_ACCESS : LogEntity.TYPE_EXCEPTION);
            logEntity.setRemoteAddr(StringUtils.getRemoteAddr(request));
            logEntity.setUserAgent(request.getHeader("user-agent"));
            logEntity.setRequestUri(request.getRequestURI());
            logEntity.setParams(request.getParameterMap());
            logEntity.setMethod(request.getMethod());
            // 异步保存日志
            new SaveLogThread(logEntity, handler, ex).start();
        }
    }

    /**
     * 保存日志线程
     */
    public static class SaveLogThread extends Thread {

        private LogEntity logEntity;
        private Object handler;
        private Exception ex;

        public SaveLogThread(LogEntity logEntity, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.logEntity = logEntity;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            // 获取日志标题
            if (StringUtils.isBlank(logEntity.getTitle())) {
                logEntity.setTitle(logEntity.getRequestUri());
            }
            // 如果有异常，设置异常信息
            logEntity.setException(Exceptions.getStackTraceAsString(ex));
            // 如果无标题并无异常日志，则不保存信息
            if (StringUtils.isBlank(logEntity.getTitle()) && StringUtils.isBlank(logEntity.getException())) {
                return;
            }
            // 保存日志信息
            logDao.save(logEntity);
        }
    }

    /**
     * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
     */
    public static String getMenuNamePath(String requestUri, String permission) {
        String href = StringUtils.substringAfter(requestUri, "");
        @SuppressWarnings("unchecked")
//        Map<String, String> menuMap = (Map<String, String>) CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
        Map<String, String> menuMap = null;
        if (menuMap == null) {
            menuMap = Maps.newHashMap();
            List<MenuEntity> menuList = menuDao.findAll();
            for (MenuEntity menu : menuList) {
                // 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
                String namePath = "";
                if (menu.getParentIds() != null && menu.getIsShow().equals(IS_SHOW)) {
                    List<String> namePathList = Lists.newArrayList();
                    for (String id : StringUtils.split(menu.getParentIds(), SEPARATOR)) {
                        if (MenuEntity.getRootId().equals(id)) {
                            continue; // 过滤跟节点
                        }
                        for (MenuEntity m : menuList) {
                            if (m.getId().equals(id)) {
                                namePathList.add(m.getName());
                                break;
                            }
                        }
                    }
                    namePathList.add(menu.getName());
                    namePath = StringUtils.join(namePathList, "-");
                }
                menuMap.put(href, namePath);
/*                // 设置菜单名称路径
                if (StringUtils.isNotBlank(menu.getHref())) {

                } else if (StringUtils.isNotBlank(menu.getPermission())) {
                    for (String p : StringUtils.split(menu.getPermission())) {
                        menuMap.put(p, namePath);
                    }
                }*/

            }
            CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
        }
        String menuNamePath = menuMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = menuMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) {
                    break;
                }
            }
            if (menuNamePath == null) {
                return "";
            }
        }
        return menuNamePath;
    }


}
