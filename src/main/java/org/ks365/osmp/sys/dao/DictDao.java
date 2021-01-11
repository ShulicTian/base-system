/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.DictEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 字典DAO接口
 *
 * @author tianslc
 */
public interface DictDao extends JpaRepository<DictEntity, Integer> {

}
