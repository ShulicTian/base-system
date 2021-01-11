package org.ks365.osmp.common.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Redis工具类
 *
 * @author tianslc
 * @date 2020/01/09
 */
public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);

    public static String get(String key) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key);
    }

    public static void set(String key, String value) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value);
    }

}
