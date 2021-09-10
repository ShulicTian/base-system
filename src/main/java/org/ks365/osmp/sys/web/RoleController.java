package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.entity.RoleEntity;
import org.ks365.osmp.sys.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @author tianslc
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequiresPermissions("sys:role:view")
    @PostMapping("list")
    public ResponseEntity<Page<RoleEntity>> list(@RequestBody RoleEntity roleEntity) {
        return new ResponseEntity<Page<RoleEntity>>().ok("获取成功").result(roleService.getPageByColumn(roleEntity));
    }

    @RequiresPermissions("sys:role:view")
    @PostMapping("allList")
    public ResponseEntity<List<RoleEntity>> allList() {
        return new ResponseEntity<List<RoleEntity>>().ok("获取成功").result(roleService.findAllList());
    }

    @RequiresPermissions("sys:role:view")
    @PostMapping("findList")
    public ResponseEntity<List<RoleEntity>> findList(@RequestBody RoleEntity roleEntity) {
        List<RoleEntity> list = roleService.getListByColumn(roleEntity);
        if (StringUtils.isNotEmpty(roleEntity.getRoleTypes())) {
            list = list.stream().filter(role -> roleEntity.getRoleTypes().contains(role.getRoleType())).collect(Collectors.toList());
        }
        return new ResponseEntity<List<RoleEntity>>().ok("获取成功").result(list);
    }

    @RequiresPermissions("sys:role:edit")
    @PostMapping("save")
    public ResponseEntity<RoleEntity> save(@RequestBody RoleEntity roleEntity) {
        return new ResponseEntity<RoleEntity>().ok("保存成功").result(roleService.save(roleEntity));
    }

    @RequiresPermissions("sys:role:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        roleService.delete(id);
        return new ResponseEntity().ok("保存成功");
    }
}
