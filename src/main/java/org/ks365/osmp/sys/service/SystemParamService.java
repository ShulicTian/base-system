package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.SystemParamDao;
import org.ks365.osmp.sys.entity.SystemParamEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SystemParamService {
    private final SystemParamDao systemParamDao;

    public SystemParamService(SystemParamDao systemParamDao) {
        this.systemParamDao = systemParamDao;
    }

    @Cacheable(value = "sysParamList", key = "'all'")
    public List<SystemParamEntity> findAllList() {
        return systemParamDao.findAll();
    }

    public Page<SystemParamEntity> getListByColumn(SystemParamEntity systemParamEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withMatcher("paramName", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<SystemParamEntity> example = Example.of(systemParamEntity, exampleMatcher);
        return systemParamDao.findAll(example, PageRequest.of(systemParamEntity.getPage() - 1, systemParamEntity.getSize(), Sort.by("paramName").and(Sort.by("updateDate").descending())));
    }

    public SystemParamEntity getOneByColumn(SystemParamEntity systemParamEntity) {
        Example<SystemParamEntity> example = Example.of(systemParamEntity);
        return systemParamDao.findOne(example).orElse(null);
    }

    @CacheEvict(value = "sysParamList", allEntries = true)
    @Transactional(readOnly = false)
    public SystemParamEntity save(SystemParamEntity systemParamEntity) {
        systemParamEntity = systemParamDao.saveAndFlush(systemParamEntity);
        return systemParamEntity;
    }

    public SystemParamEntity getById(Integer id) {
        return systemParamDao.getOne(id);
    }

    @CacheEvict(value = "sysParamList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(SystemParamEntity systemParamEntity) {
        systemParamDao.delete(systemParamEntity);
    }

    @CacheEvict(value = "sysParamList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        SystemParamEntity systemParamEntity = new SystemParamEntity();
        systemParamEntity.setId(id);
        delete(systemParamEntity);
    }
}
