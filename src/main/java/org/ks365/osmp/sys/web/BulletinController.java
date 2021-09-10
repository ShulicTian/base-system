package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.sys.entity.BulletinEntity;
import org.ks365.osmp.sys.service.BulletinService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统公告Controller
 *
 * @author tianslc
 */
@RestController
@RequestMapping(value = "/system/bulletin")
public class BulletinController extends BaseController {

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    @RequiresPermissions("system:bulletin:view")
    @PostMapping("list")
    public ResponseEntity<Page<BulletinEntity>> list(@RequestBody BulletinEntity bulletinEntity) {
        return new ResponseEntity<Page<BulletinEntity>>().ok("获取成功").result(bulletinService.getPageByColumn(bulletinEntity));
    }

    @RequiresPermissions("system:bulletin:view")
    @PostMapping("findList")
    public ResponseEntity<List<BulletinEntity>> findList(@RequestBody BulletinEntity bulletinEntity) {
        return new ResponseEntity<List<BulletinEntity>>().ok("获取成功").result(bulletinService.getListByColumn(bulletinEntity));
    }

    @RequiresPermissions("system:bulletin:edit")
    @PostMapping("save")
    public ResponseEntity<BulletinEntity> save(@RequestBody BulletinEntity bulletinEntity) {
        return new ResponseEntity<BulletinEntity>().ok("保存成功").result(bulletinService.save(bulletinEntity));
    }

    @RequiresPermissions("system:bulletin:edit")
    @PostMapping("updateBulletinStatus")
    public ResponseEntity<BulletinEntity> updateBulletinStatus() {
        return new ResponseEntity<BulletinEntity>().ok("更新成功").resultByCode(bulletinService.updateBulletinStatus() + "");
    }

    @RequiresPermissions("system:bulletin:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        bulletinService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }


    @RequiresPermissions("system:bulletin:view")
    @PostMapping("getActiveList")
    public ResponseEntity<List<BulletinEntity>> getActiveList(@RequestBody BulletinEntity bulletinEntity) {
        return new ResponseEntity<List<BulletinEntity>>().ok("获取成功").result(bulletinService.getActiveList(bulletinEntity));
    }
}
