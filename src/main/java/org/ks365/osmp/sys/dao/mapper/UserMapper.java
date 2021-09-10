package org.ks365.osmp.sys.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.ks365.osmp.sys.entity.UserEntity;

import java.util.List;

public interface UserMapper {

    void updateUserInfo(UserEntity userEntity);

    List<UserEntity> findListBySelector(UserEntity userEntity);

    UserEntity getByMobile(String mobile);

    List<UserEntity> getUserByUserNameAndPhone(@Param("username") String username, @Param("phone") String phone);
}
