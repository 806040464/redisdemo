package com.zcc.redis.lock;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component("jedisWithReentrantLock")
public class JedisWithReentrantLock {
    private ThreadLocal<Map<String, AtomicInteger>> lockers = new ThreadLocal<>();

    private JedisPool jedisPool;

    public JedisWithReentrantLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private boolean redisLock(String key) {
        Jedis jedis = jedisPool.getResource();
        return null != jedis.set(key, "1", "NX", "EX", 500);
    }

    private void redisUnlock(String key) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(key);
    }

    private Map<String, AtomicInteger> currentLockers() {
        Map<String, AtomicInteger> refs = lockers.get();
        if (null != refs) {
            return refs;
        }
        lockers.set(new HashMap<>());
        return lockers.get();
    }

    public boolean lock(String key) {
        Map<String, AtomicInteger> refs = currentLockers();
        AtomicInteger refCnt = refs.get(key);
        if (refCnt != null) {
            refCnt.incrementAndGet();
            refs.put(key, refCnt);
            return true;
        }
        boolean ok = this.redisLock(key);
        if (!ok) {
            return false;
        }
        refs.put(key, new AtomicInteger(1));
        return true;
    }

    public boolean unlock(String key) {
        Map<String, AtomicInteger> refs = currentLockers();
        AtomicInteger refCnt = refs.get(key);
        if (null == refCnt) {
            return false;
        }
        refCnt.decrementAndGet();
        if (0 < refCnt.get()) {
            refs.put(key, refCnt);
        } else {
            refs.remove(key);
            this.redisUnlock(key);
        }
        return true;
    }
}
