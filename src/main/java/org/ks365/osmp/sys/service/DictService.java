/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.DictDao;
import org.ks365.osmp.sys.entity.DictEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 字典Service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class DictService {
    private final DictDao dictDao;

    public DictService(DictDao dictDao) {
        this.dictDao = dictDao;
    }

    @Cacheable(value = "dictList", key = "'all'")
    public List<DictEntity> findAllList() {
        return dictDao.findAll();
    }

    public Page<DictEntity> getListByColunm(DictEntity dictEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withMatcher("type", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("label", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<DictEntity> example = Example.of(dictEntity, exampleMatcher);
        return dictDao.findAll(example, PageRequest.of(dictEntity.getPage() - 1, dictEntity.getSize(), Sort.by("type", "value")));
    }

    public DictEntity getOneByColunm(DictEntity dictEntity) {
        Example<DictEntity> example = Example.of(dictEntity);
        Optional<DictEntity> optional = dictDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "dictList", allEntries = true)
    @Transactional(readOnly = false)
    public DictEntity save(DictEntity dictEntity) {
        dictEntity = dictDao.saveAndFlush(dictEntity);
        return dictEntity;
    }

    public DictEntity getById(Integer id) {
        return dictDao.getOne(id);
    }

    @CacheEvict(value = "dictList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(DictEntity dictEntity) {
        dictDao.delete(dictEntity);
    }

    @CacheEvict(value = "dictList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        DictEntity dictEntity = new DictEntity();
        dictEntity.setId(id);
        delete(dictEntity);
    }
}
