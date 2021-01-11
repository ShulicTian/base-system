/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.MenuEntity;
import org.ks365.osmp.sys.service.MenuService;
import org.ks365.osmp.sys.utils.MenuUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单Controller
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("getMenuList")
    public ResponseEntity<List<MenuEntity>> getMenuList() {
        return new ResponseEntity<List<MenuEntity>>().ok("获取成功").result(MenuUtils.getUserMenuList());
    }

    @RequiresPermissions("sys:menu:view")
    @PostMapping("list")
    public ResponseEntity<List<MenuEntity>> list(@RequestBody MenuEntity menuEntity) {
        Set<MenuEntity> list = new HashSet<>();
        list.addAll(menuService.getListByColunm(menuEntity));
        menuEntity.setId(0);
        MenuUtils.treeMenus(list, menuEntity);
        return new ResponseEntity<List<MenuEntity>>().ok("获取成功").result(menuEntity.getChildren());
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping("save")
    public ResponseEntity<MenuEntity> save(@RequestBody MenuEntity menuEntity) {
        return new ResponseEntity<MenuEntity>().ok("保存成功").result(menuService.save(menuEntity));
    }

    @RequiresPermissions("sys:menu:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        menuService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }

}
