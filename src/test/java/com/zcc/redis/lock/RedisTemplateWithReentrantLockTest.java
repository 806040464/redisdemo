package com.zcc.redis.lock;

import com.zcc.redis.RedisdemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisdemoApplication.class)
public class RedisTemplateWithReentrantLockTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void lock() {
        RedisTemplateWithReentrantLock redis = new RedisTemplateWithReentrantLock(redisTemplate);
        boolean redislock = redis.lock("redislock", "1", 500);
        System.out.println(redislock);
    }

    @Test
    public void unlock() {
        RedisTemplateWithReentrantLock redis = new RedisTemplateWithReentrantLock(redisTemplate);
        boolean unlock = redis.unlock("redislock");
        System.out.println(unlock);
    }
}