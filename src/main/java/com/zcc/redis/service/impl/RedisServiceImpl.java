package com.zcc.redis.service.impl;

import com.zcc.redis.service.RedisService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void put(Object key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void putAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public Object get(Object key, Object hashKey) {
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        return o;
    }

    @Override
    public List getAllUser(String key, Collection<String> hashKey) {
        List list = redisTemplate.opsForHash().multiGet(key, hashKey);
        return list;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
