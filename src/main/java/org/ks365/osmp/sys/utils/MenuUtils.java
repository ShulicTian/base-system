package org.ks365.osmp.sys.utils;

import org.ks365.osmp.sys.entity.MenuEntity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单工具类
 *
 * @author tianslc
 */
public class MenuUtils {

    public static List<MenuEntity> getUserMenuListByType(String type) {
        Set<MenuEntity> userMenu = new HashSet<>(UserUtils.getMenuList());
        MenuEntity root = userMenu.stream().filter(menuEntity -> menuEntity.getId().equals(0)).collect(Collectors.toList()).get(0);
        root.setType(type);
        treeMenus(userMenu, root);
        return root.getChildren();
    }

    public static void treeMenus(Set<MenuEntity> userMenu, MenuEntity nowMenu) {
        if (userMenu != null && userMenu.size() > 0) {
            List<MenuEntity> children = userMenu.stream()
                    .filter(menuEntity ->
                            menuEntity.getParentId().equals(nowMenu.getId())
                                    && !menuEntity.getId().equals(0)
                                    && menuEntity.getType().equals(nowMenu.getType())).collect(Collectors.toList());
            if (children.size() > 0) {
                children.sort(Comparator.comparing(MenuEntity::getSort));
                nowMenu.setChildren(children);
                children.forEach(menuEntity -> {
                    treeMenus(userMenu, menuEntity);
                });
            }
        }
    }

}
