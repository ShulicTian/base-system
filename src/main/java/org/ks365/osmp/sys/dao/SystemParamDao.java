/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.AreaEntity;
import org.ks365.osmp.sys.entity.SystemParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 区域DAO接口
 *
 * @author tianslc
 */
public interface SystemParamDao extends JpaRepository<SystemParamEntity, Integer> {
}
