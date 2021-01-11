package org.ks365.osmp.sys.dao;

import org.apache.ibatis.annotations.Param;
import org.ks365.osmp.sys.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * UserDao For JPA
 *
 * @author tianslc
 */
public interface UserDao extends JpaRepository<UserEntity, Integer> {

    /**
     * 根据ID获取用户名
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT name FROM sys_user WHERE id = :id", nativeQuery = true)
    String getUserNameById(@Param("id") Integer id);

    /**
     * 修改密码
     *
     * @param userEntity
     */
    @Modifying
    @Query(value = "UPDATE UserEntity user SET user.password=:#{#userEntity.password} WHERE user.id=:#{#userEntity.id}")
    void updatePassword(@Param("userEntity") UserEntity userEntity);
}
