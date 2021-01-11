package org.ks365.osmp;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.ks365.osmp.sys.dao.UserDao;
import org.ks365.osmp.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.Optional;

@SpringBootTest
class OsmpApplicationTests {

    @Autowired
    private UserDao userDao;


    @Test
    void contextLoads() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("admin");
        Example<UserEntity> example = Example.of(userEntity);
        Optional<UserEntity> optional = userDao.findOne(example);
        UserEntity user = optional.get();
        System.out.println(JSON.parseObject(JSON.toJSONString(user), UserEntity.class).getCreateBy());
    }

}
