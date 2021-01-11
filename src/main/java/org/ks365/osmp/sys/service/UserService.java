package org.ks365.osmp.sys.service;

import org.apache.shiro.SecurityUtils;
import org.ks365.osmp.common.utils.AuthUtils;
import org.ks365.osmp.sys.dao.UserDao;
import org.ks365.osmp.sys.dao.mapper.UserMapper;
import org.ks365.osmp.sys.entity.UserEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户管理Service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserService(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }


    public UserEntity getUserByUserName(String userName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userName);
        return getOneByColunm(userEntity);
    }

    public Page<UserEntity> getListByColunm(UserEntity userEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("username", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("idCard", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("mobile", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<UserEntity> example = Example.of(userEntity, exampleMatcher);
        return userDao.findAll(example, PageRequest.of(userEntity.getPage() - 1, userEntity.getSize(), Sort.by("name", "username")));
    }

    public UserEntity getOneByColunm(UserEntity userEntity) {
        Example<UserEntity> example = Example.of(userEntity);
        Optional<UserEntity> optional = userDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "userName", key = "#user.getId()")
    @Transactional(readOnly = false)
    public UserEntity save(UserEntity user) {
        return userDao.saveAndFlush(user);
    }

    public UserEntity getById(Integer id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return getOneByColunm(userEntity);
    }

    @CacheEvict(value = "userName", key = "#userEntity.getId()")
    @Transactional(readOnly = false)
    public void delete(UserEntity userEntity) {
        userDao.delete(userEntity);
    }

    @CacheEvict(value = "userName", key = "#id")
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        delete(userEntity);
    }

    @Cacheable(value = "userName", key = "#id")
    public String getUserNameById(Integer id) {
        return userDao.getUserNameById(id);
    }

    @CacheEvict(value = "userName", key = "#id")
    @Transactional(readOnly = false)
    public void updateUserInfo(UserEntity userEntity) {
        userMapper.updateUserInfo(userEntity);
    }

    @Transactional(readOnly = false)
    public void updatePassword(UserEntity userEntity) {
        userEntity.setPassword(AuthUtils.entryptPassword(userEntity.getPassword()));
        userDao.updatePassword(userEntity);
        SecurityUtils.getSubject().logout();
    }

    public Boolean isExist(UserEntity userEntity) {
        Example<UserEntity> example = Example.of(userEntity);
        return userDao.exists(example);
    }
}
