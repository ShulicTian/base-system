package org.ks365.osmp.sys.utils;

import org.ks365.osmp.common.utils.SpringContextHolder;
import org.ks365.osmp.sys.dao.RoleDao;
import org.ks365.osmp.sys.entity.RoleEntity;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

/**
 * @author tianslc
 */
public class RoleUtils {
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);

    public static RoleEntity getByEnName(String enName) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setEnName(enName);
        return get(roleEntity);
    }

    public static RoleEntity get(RoleEntity roleEntity) {
        Example<RoleEntity> example = Example.of(roleEntity);
        Optional<RoleEntity> optional = roleDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public static List<RoleEntity> findAll() {
        return roleDao.findAll();
    }

    public static List<RoleEntity> findList(RoleEntity role) {
        Example<RoleEntity> example = Example.of(role);
        return roleDao.findAll(example);
    }
}
