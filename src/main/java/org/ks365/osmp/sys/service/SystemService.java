package org.ks365.osmp.sys.service;

import org.ks365.osmp.common.utils.AuthUtils;
import org.ks365.osmp.sys.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统基本功能
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class SystemService {

    private final UserService userService;

    public SystemService(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = false)
    public UserEntity registerUser(UserEntity user) {
        if (user.getId() == null) {
            //解决级联报错问题
            user.setId(-1);
        }
        user.setPassword(AuthUtils.entryptPassword(user.getPassword()));
        user = userService.save(user);
        user.setCreateBy(user.getId());
        user.setUpdateBy(user.getId());
        return userService.save(user);
    }
}
