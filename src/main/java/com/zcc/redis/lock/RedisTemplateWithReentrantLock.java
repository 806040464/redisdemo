package com.zcc.redis.lock;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

@Component("redisTemplateWithReentrantLock")
public class RedisTemplateWithReentrantLock {

    private RedisTemplate redisTemplate;

    public boolean lock(String key, String value, long expireTime) {
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                Object execute = redisConnection.execute("set", keySerializer.serialize(key),
                        valueSerializer.serialize(value),
                        SafeEncoder.encode("NX"),
                        SafeEncoder.encode("EX"),
                        Protocol.toByteArray(expireTime));
                return null != execute;
            }
        });
        return result;
    }

    public boolean unlock(String key) {
        boolean delete = redisTemplate.delete(key);
        return delete;
    }

    public RedisTemplateWithReentrantLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
