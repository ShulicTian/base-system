/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.AreaEntity;
import org.ks365.osmp.sys.service.AreaService;
import org.ks365.osmp.sys.utils.AreaUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 区域管理
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/area")
public class AreaController extends BaseController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @RequiresPermissions("sys:area:view")
    @PostMapping("list")
    public ResponseEntity<List<AreaEntity>> list(@RequestBody AreaEntity areaEntity) {
        return new ResponseEntity<List<AreaEntity>>().ok("获取成功").result(areaService.getListByColunm(areaEntity));
    }

    @RequiresPermissions("sys:area:edit")
    @PostMapping("save")
    public ResponseEntity<AreaEntity> save(@RequestBody AreaEntity areaEntity) {
        return new ResponseEntity<AreaEntity>().ok("保存成功").result(areaService.save(areaEntity));
    }

    @RequiresPermissions("sys:area:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        areaService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }
}
