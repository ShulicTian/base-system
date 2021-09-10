package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 系统菜单持久层
 *
 * @author tianslc
 * @date 2020/01/16
 */
public interface MenuDao extends JpaRepository<MenuEntity, Integer> {

    /**
     * 删除中间表
     *
     * @param id
     */
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_role_menu WHERE menu_id = :id")
    void deleteWithRole(Integer id);
}
