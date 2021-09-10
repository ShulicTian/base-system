package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.BulletinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 系统公告DAO接口
 *
 * @author tianslc
 */
public interface BulletinDao extends JpaRepository<BulletinEntity, Integer> {

    /**
     * 更新已过期公告状态
     *
     * @return
     */
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE sys_bulletin SET status = '0' WHERE create_date<SUBDATE(CURRENT_TIMESTAMP,duration)")
    int updateBulletinStatus();

    /**
     * 获取存活的公告
     *
     * @param bulletinEntity
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT * FROM sys_bulletin WHERE create_date>SUBDATE(CURRENT_TIMESTAMP,duration) AND type=:#{#bulletinEntity.type} AND flag = 0 ORDER BY create_date DESC")
    List<BulletinEntity> getActiveList(BulletinEntity bulletinEntity);
}
