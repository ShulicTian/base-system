package org.ks365.osmp.sys.dao;

import org.ks365.osmp.sys.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 消息管理DAO接口
 *
 * @author tianslc
 */
public interface MessageDao extends JpaRepository<MessageEntity, Integer> {
}
