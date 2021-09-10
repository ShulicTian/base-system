package org.ks365.osmp.common.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis配置
 *
 * @author tianslc
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private String host;
    private Integer port;
    private String password;
    private int timeout;
    private Lettuce lettuce;
}

@Data
class Lettuce {
    private Pool pool;
}

@Data
class Pool {
    private Integer maxActive;
    private Integer maxIdle;
    private Integer minIdle;
}
