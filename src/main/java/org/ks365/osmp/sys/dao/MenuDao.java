package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 系统菜单持久层
 *
 * @author tianslc
 * @date 2020/01/16
 */
public interface MenuDao extends JpaRepository<MenuEntity, Integer> {

}
