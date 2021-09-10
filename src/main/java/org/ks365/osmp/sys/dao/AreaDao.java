
package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 区域DAO接口
 *
 * @author tianslc
 */
public interface AreaDao extends JpaRepository<AreaEntity, Integer> {

    @Query(nativeQuery = true, value = "SELECT t2.* FROM sys_area t1 " +
            "JOIN sys_area t2 ON (find_in_set(t2.id,t1.parent_ids) OR t2.id = :areaId) AND t2.type = 2 " +
            "WHERE t1.id = :areaId")
    AreaEntity findCityByChildAreaId(String areaId);

}
