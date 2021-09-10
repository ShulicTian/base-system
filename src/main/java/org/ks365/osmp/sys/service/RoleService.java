package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.RoleDao;
import org.ks365.osmp.sys.entity.RoleEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 角色管理Service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * 根据角色名称获取角色
     *
     * @param name
     * @return
     */
    public RoleEntity getRoleByName(String name) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        return getOneByColumn(roleEntity);
    }

    /**
     * 根据角色英文名称获取角色
     *
     * @param enName
     * @return
     */
    public RoleEntity getRoleByEnName(String enName) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setEnName(enName);
        return getOneByColumn(roleEntity);
    }

    public Page<RoleEntity> getPageByColumn(RoleEntity roleEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("enName", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<RoleEntity> example = Example.of(roleEntity, exampleMatcher);
        return roleDao.findAll(example, PageRequest.of(roleEntity.getPage() - 1, roleEntity.getSize(), Sort.by("roleType").and(Sort.by("enName"))));
    }

    public List<RoleEntity> getListByColumn(RoleEntity roleEntity) {
        Example<RoleEntity> example = Example.of(roleEntity);
        return roleDao.findAll(example, Sort.by("roleType").and(Sort.by("enName")));
    }

    @Cacheable(value = "roleList", key = "'all'")
    public List<RoleEntity> findAllList() {
        return roleDao.findAll();
    }

    public RoleEntity getOneByColumn(RoleEntity roleEntity) {
        Example<RoleEntity> example = Example.of(roleEntity);
        Optional<RoleEntity> optional = roleDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @Transactional(readOnly = false)
    public RoleEntity save(RoleEntity roleEntity) {
        roleEntity = roleDao.saveAndFlush(roleEntity);
        return roleEntity;
    }

    public RoleEntity getById(Integer id) {
        return roleDao.getOne(id);
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(RoleEntity roleEntity) {
        roleDao.delete(roleEntity);
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        roleDao.deleteWithUser(id);
        deleteById(id);
    }

    @CacheEvict(value = "roleList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        delete(roleEntity);
    }
}
