package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 日志DAO接口
 *
 * @author tianslc
 * @date 2020/01/16
 */
public interface LogDao extends JpaRepository<LogEntity, Integer>, JpaSpecificationExecutor<LogEntity> {

}
