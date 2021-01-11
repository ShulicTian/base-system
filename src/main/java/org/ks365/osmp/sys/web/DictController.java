/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.DictEntity;
import org.ks365.osmp.sys.service.DictService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/dict")
public class DictController extends BaseController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @RequiresPermissions("sys:dict:view")
    @PostMapping("list")
    public ResponseEntity<Page<DictEntity>> list(@RequestBody DictEntity dictEntity) {
        return new ResponseEntity<Page<DictEntity>>().ok("获取成功").result(dictService.getListByColunm(dictEntity));
    }

    @RequiresPermissions("sys:dict:view")
    @PostMapping("allList")
    public ResponseEntity<List<DictEntity>> allList() {
        return new ResponseEntity<List<DictEntity>>().ok("获取成功").result(dictService.findAllList());
    }

    @RequiresPermissions("sys:dict:edit")
    @PostMapping("save")
    public ResponseEntity<DictEntity> save(@RequestBody DictEntity dictEntity) {
        return new ResponseEntity<DictEntity>().ok("保存成功").result(dictService.save(dictEntity));
    }

    @RequiresPermissions("sys:dict:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        dictService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }
}
