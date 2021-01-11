package org.ks365.osmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 启动类
 *
 * @author tianslc
 */
@MapperScan("org.ks365.osmp.**.dao.mapper")
@EnableJpaAuditing
@SpringBootApplication
public class OsmpApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsmpApplication.class, args);
    }

}
