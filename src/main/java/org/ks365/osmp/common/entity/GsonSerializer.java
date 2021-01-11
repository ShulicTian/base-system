package org.ks365.osmp.common.entity;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class GsonSerializer implements RedisSerializer<String> {
    @Override
    public byte[] serialize(String s) throws SerializationException {
        System.err.println(s);
        return new byte[0];
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        System.err.println(bytes);
        return null;
    }
}
