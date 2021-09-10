
package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.DictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 字典DAO接口
 *
 * @author tianslc
 */
public interface DictDao extends JpaRepository<DictEntity, Integer> {

    @Query(nativeQuery = true, value = "select * from sys_dict where type=?1")
    List<DictEntity> findListByType(String type);
}
