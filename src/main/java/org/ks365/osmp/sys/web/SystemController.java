package org.ks365.osmp.sys.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.ks365.osmp.common.ctrl.BaseController;
import org.ks365.osmp.common.entity.ResponseEntity;
import org.ks365.osmp.common.security.UsernamePasswordToken;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.entity.UserEntity;
import org.ks365.osmp.sys.entity.UserGroup;
import org.ks365.osmp.sys.service.SystemService;
import org.ks365.osmp.sys.utils.UserUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统基本操作Controller
 *
 * @author tianslc
 */
@RestController
@RequestMapping("/sys/system")
public class SystemController extends BaseController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    /**
     * 登录
     *
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "login")
    public ResponseEntity<Object> login(HttpServletResponse response) {
        ResponseEntity<Object> responseEntity = new ResponseEntity<Object>();
        responseEntity.faild("登录失效");
        response.setStatus(401);
        return responseEntity;
    }

    @GetMapping(value = "unauthorized")
    public ResponseEntity unauthorized(HttpServletResponse response) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.faild("您没有权限");
        response.setStatus(302);
        return responseEntity;
    }

    /**
     * 登录
     *
     * @param userEntity
     * @return
     */
    @PostMapping(value = "login")
    public ResponseEntity<UserEntity> login(@RequestBody UserEntity userEntity) {
        ResponseEntity<UserEntity> result = new ResponseEntity<>();
        if (StringUtils.isEmpty(userEntity.getUsername()) || StringUtils.isEmpty(userEntity.getPassword())) {
            return result.faild("账号密码不能为空");
        }
        UserEntity user = UserUtils.getByUserName(userEntity.getUsername());
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {

            UsernamePasswordToken token = new UsernamePasswordToken(userEntity.getUsername(), userEntity.getPassword().toCharArray(), userEntity.isRememberMe(), "");
            try {
                currentUser.login(token);
                user.setToken(String.valueOf(currentUser.getSession().getId()));
                result.ok("登录成功").result(user);
            } catch (UnknownAccountException ex) {
                ex.printStackTrace();
                logger.warn("账号错误");
                result.faild("账号错误");
            } catch (IncorrectCredentialsException ex) {
                ex.printStackTrace();
                logger.warn("密码错误");
                result.faild("密码错误");
            } catch (LockedAccountException ex) {
                ex.printStackTrace();
                logger.warn("账号已被锁定，请与系统管理员联系");
                result.faild("账号已被锁定，请与系统管理员联系");
            } catch (AuthenticationException ex) {
                ex.printStackTrace();
                logger.warn("您没有授权!");
                result.faild("您没有授权");
            }
        } else {
            result.ok("请勿重复登入").result(user);
        }
        return result;
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping(value = "logout")
    public ResponseEntity<Boolean> logout() {
        ResponseEntity<Boolean> result = new ResponseEntity<>();
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            UserUtils.clearCache();
            currentUser.logout();
            result.ok("登出成功").result(true);
        } else {
            result.faild("您还未登录").result(false);
        }
        return result;
    }

    /**
     * 注册用户、更改用户信息
     *
     * @return
     */
    @PostMapping(value = "registerUser")
    public ResponseEntity<UserEntity> registerUser(@RequestBody @Validated(UserGroup.class) UserEntity user) {
        ResponseEntity<UserEntity> result = new ResponseEntity<>();
        user = systemService.registerUser(user);
        return result.ok("保存成功").result(user);
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    @PostMapping(value = "getCurrentUser")
    public ResponseEntity<UserEntity> getCurrentUser() {
        ResponseEntity<UserEntity> result = new ResponseEntity<>();
        return result.ok("获取成功").result(UserUtils.getCurrentUser());
    }

}
