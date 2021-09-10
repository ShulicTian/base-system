package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.BulletinDao;
import org.ks365.osmp.sys.entity.BulletinEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统公告service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class BulletinService {
    private final BulletinDao bulletinDao;

    public BulletinService(BulletinDao bulletinDao) {
        this.bulletinDao = bulletinDao;
    }

    @Cacheable(value = "bulletinList", key = "'all'")
    public List<BulletinEntity> findAllList() {
        return bulletinDao.findAll();
    }

    public Page<BulletinEntity> getPageByColumn(BulletinEntity bulletinEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<BulletinEntity> example = Example.of(bulletinEntity, exampleMatcher);
        return bulletinDao.findAll(example, PageRequest.of(bulletinEntity.getPage() - 1, bulletinEntity.getSize(), Sort.by("createDate")));
    }

    public List<BulletinEntity> getListByColumn(BulletinEntity bulletinEntity) {
        Example<BulletinEntity> example = Example.of(bulletinEntity);
        return bulletinDao.findAll(example, Sort.by("createDate"));
    }

    public BulletinEntity getOneByColumn(BulletinEntity bulletinEntity) {
        Example<BulletinEntity> example = Example.of(bulletinEntity);
        Optional<BulletinEntity> optional = bulletinDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "bulletinList", allEntries = true)
    @Transactional(readOnly = false)
    public BulletinEntity save(BulletinEntity bulletinEntity) {
        bulletinEntity = bulletinDao.saveAndFlush(bulletinEntity);
        return bulletinEntity;
    }

    public BulletinEntity getById(Integer id) {
        return bulletinDao.getOne(id);
    }

    @CacheEvict(value = "bulletinList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(BulletinEntity bulletinEntity) {
        bulletinDao.delete(bulletinEntity);
    }

    @CacheEvict(value = "bulletinList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        BulletinEntity bulletinEntity = new BulletinEntity();
        bulletinEntity.setId(id);
        delete(bulletinEntity);
    }

    @Transactional(readOnly = false)
    public int updateBulletinStatus() {
        return bulletinDao.updateBulletinStatus();
    }

    public List<BulletinEntity> getActiveList(BulletinEntity bulletinEntity) {
        return bulletinDao.getActiveList(bulletinEntity);
    }
}
