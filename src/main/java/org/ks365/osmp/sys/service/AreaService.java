
package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.AreaDao;
import org.ks365.osmp.sys.entity.AreaEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 区域Service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class AreaService {
    private final AreaDao areaDao;

    public AreaService(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    @Cacheable(value = "areaList", key = "'all'")
    public List<AreaEntity> findAllList() {
        return areaDao.findAll();
    }

    public List<AreaEntity> getListByColumn(AreaEntity areaEntity) {
        Example<AreaEntity> example = Example.of(areaEntity);
        return areaDao.findAll(example, Sort.by("sort"));
    }

    public AreaEntity getOneByColumn(AreaEntity areaEntity) {
        Example<AreaEntity> example = Example.of(areaEntity);
        Optional<AreaEntity> optional = areaDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "areaList", allEntries = true)
    @Transactional(readOnly = false)
    public AreaEntity save(AreaEntity areaEntity) {
        areaEntity = areaDao.saveAndFlush(areaEntity);
        reSortChildren(areaEntity);
        return areaEntity;
    }

    @CacheEvict(value = "menuList", allEntries = true)
    @Transactional(readOnly = false)
    public void reSortChildren(AreaEntity parent) {
        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setParentId(parent.getId());
        List<AreaEntity> list = getListByColumn(areaEntity);
        list.forEach(area -> {
            area.setParentIds(parent.getParentIds() + parent.getId() + ",");
        });
        areaDao.saveAll(list);
    }

    public AreaEntity getById(Integer id) {
        return areaDao.getOne(id);
    }

    @CacheEvict(value = "areaList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(AreaEntity areaEntity) {
        areaDao.delete(areaEntity);
    }

    @CacheEvict(value = "areaList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        AreaEntity areaEntity = new AreaEntity();
        areaEntity.setId(id);
        delete(areaEntity);
    }
}
