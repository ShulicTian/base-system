package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.MenuDao;
import org.ks365.osmp.sys.entity.MenuEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统菜单相关操作
 *
 * @author tianslc
 * @date 2020/01/16
 */
@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;

    public MenuService(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Cacheable(value = "menuList", key = "'all'")
    public List<MenuEntity> findAllList() {
        return menuDao.findAll();
    }

    public List<MenuEntity> getListByColunm(MenuEntity menuEntity) {
        Example<MenuEntity> example = Example.of(menuEntity);
        return menuDao.findAll(example);
    }

    public MenuEntity getOneByColunm(MenuEntity menuEntity) {
        Example<MenuEntity> example = Example.of(menuEntity);
        Optional<MenuEntity> optional = menuDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "menuList", allEntries = true)
    @Transactional(readOnly = false)
    public MenuEntity save(MenuEntity menuEntity) {
        return menuDao.saveAndFlush(menuEntity);
    }

    @CacheEvict(value = "menuList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(MenuEntity menuEntity) {
        menuDao.delete(menuEntity);
    }

    @CacheEvict(value = "menuList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(id);
        delete(menuEntity);
    }

}
