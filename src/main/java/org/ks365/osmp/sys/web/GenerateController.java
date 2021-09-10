
package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.GenerateEntity;
import org.ks365.osmp.sys.service.GenerateService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成接口
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/sys/generate")
public class GenerateController extends BaseController {

    private final GenerateService generateService;

    public GenerateController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @RequiresPermissions("sys:generate:view")
    @PostMapping("list")
    public ResponseEntity<Page<GenerateEntity>> list(@RequestBody GenerateEntity generateEntity) {
        return new ResponseEntity<Page<GenerateEntity>>().ok("获取成功").result(generateService.getPageByColumn(generateEntity));
    }

    @RequiresPermissions("sys:generate:edit")
    @PostMapping("save")
    public ResponseEntity<GenerateEntity> save(@RequestBody GenerateEntity generateEntity) {
        return new ResponseEntity<GenerateEntity>().ok("保存成功").result(generateService.save(generateEntity));
    }

    @RequiresPermissions("sys:generate:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        generateService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }

    @RequiresPermissions("sys:generate:view")
    @PostMapping("entityList")
    public ResponseEntity<List<GenerateEntity>> entityList() {
        return new ResponseEntity<List<GenerateEntity>>().ok("获取成功").result(generateService.getEntityList());
    }
}
