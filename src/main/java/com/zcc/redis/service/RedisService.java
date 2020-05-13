package com.zcc.redis.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RedisService {
    void put(Object key, Object hashKey, Object value);

    void putAll(String key, Map<String, Object> map);

    Object get(Object key, Object hashKey);

    List getAllUser(String key, Collection<String> hashKey);
}
