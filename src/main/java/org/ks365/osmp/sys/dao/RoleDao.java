package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<RoleEntity, Integer> {

    /**
     * 删除中间表
     *
     * @param id
     */
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM sys_user_role WHERE role_id = :id")
    void deleteWithUser(Integer id);
}
