package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.MessageDao;
import org.ks365.osmp.sys.entity.MessageEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 消息管理service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageDao messageDao;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Cacheable(value = "messageList", key = "'all'")
    public List<MessageEntity> findAllList() {
        return messageDao.findAll();
    }

    public Page<MessageEntity> getPageByColumn(MessageEntity messageEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<MessageEntity> example = Example.of(messageEntity, exampleMatcher);
        return messageDao.findAll(example, PageRequest.of(messageEntity.getPage() - 1, messageEntity.getSize(), Sort.by("createDate")));
    }

    public List<MessageEntity> getListByColumn(MessageEntity messageEntity) {
        Example<MessageEntity> example = Example.of(messageEntity);
        return messageDao.findAll(example, Sort.by("createDate"));
    }

    public MessageEntity getOneByColumn(MessageEntity messageEntity) {
        Example<MessageEntity> example = Example.of(messageEntity);
        Optional<MessageEntity> optional = messageDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "messageList", allEntries = true)
    @Transactional(readOnly = false)
    public MessageEntity save(MessageEntity messageEntity) {
        messageEntity = messageDao.saveAndFlush(messageEntity);
        return messageEntity;
    }

    public MessageEntity getById(Integer id) {
        return messageDao.getOne(id);
    }

    @CacheEvict(value = "messageList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(MessageEntity messageEntity) {
        messageDao.delete(messageEntity);
    }

    @CacheEvict(value = "messageList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(id);
        delete(messageEntity);
    }
}
