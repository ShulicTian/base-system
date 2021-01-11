/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.SystemParamEntity;
import org.ks365.osmp.sys.service.SystemParamService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统参数
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/param")
public class SystemParamController extends BaseController {

    private final SystemParamService systemParamService;

    public SystemParamController(SystemParamService systemParamService) {
        this.systemParamService = systemParamService;
    }

    @RequiresPermissions("sys:param:view")
    @PostMapping("list")
    public ResponseEntity<Page<SystemParamEntity>> list(@RequestBody SystemParamEntity systemParamEntity) {
        return new ResponseEntity<Page<SystemParamEntity>>().ok("获取成功").result(systemParamService.getListByColunm(systemParamEntity));
    }

    @RequiresPermissions("sys:param:edit")
    @PostMapping("save")
    public ResponseEntity<SystemParamEntity> save(@RequestBody SystemParamEntity systemParamEntity) {
        return new ResponseEntity<SystemParamEntity>().ok("保存成功").result(systemParamService.save(systemParamEntity));
    }

    @RequiresPermissions("sys:param:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        systemParamService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }

}
