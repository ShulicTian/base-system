package org.ks365.osmp.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.common.utils.AuthUtils;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.entity.UserEntity;
import org.ks365.osmp.sys.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author tianslc
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequiresPermissions("sys:user:view")
    @PostMapping("list")
    public ResponseEntity<Page<UserEntity>> list(@RequestBody UserEntity userEntity) {
        return new ResponseEntity<Page<UserEntity>>().ok("获取成功").result(userService.getListByColunm(userEntity));
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("save")
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity userEntity) {
        if (StringUtils.isNotEmpty(userEntity.getPassword())) {
            AuthUtils.entryptPassword(userEntity.getPassword());
        } else {
            if (userEntity.getId() != null) {
                UserEntity user = userService.getById(userEntity.getId());
                if (user != null) {
                    userEntity.setPassword(user.getPassword());
                } else {
                    return new ResponseEntity<UserEntity>().faild("密码不能为空");
                }
            }
        }
        return new ResponseEntity<UserEntity>().ok("保存成功").result(userService.save(userEntity));
    }

    @RequiresPermissions("sys:user:edit")
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return new ResponseEntity().ok("保存成功");
    }

    @RequiresPermissions("sys:user:view")
    @PostMapping("getUserName/{id}")
    public ResponseEntity<String> getUserName(@PathVariable Integer id) {
        return new ResponseEntity<String>().ok("查询成功").result(userService.getUserNameById(id));
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("updateUserInfo")
    public ResponseEntity updateUserInfo(@RequestBody UserEntity userEntity) {
        userService.updateUserInfo(userEntity);
        return new ResponseEntity().ok("修改成功");
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("updatePassword")
    public ResponseEntity updatePassword(@RequestBody UserEntity userEntity) {
        userService.updatePassword(userEntity);
        return new ResponseEntity().ok("修改成功");
    }

    @PostMapping("isExist")
    public ResponseEntity isExist(@RequestBody UserEntity userEntity) {
        return new ResponseEntity().ok("请求成功").result(userService.isExist(userEntity));
    }
}
