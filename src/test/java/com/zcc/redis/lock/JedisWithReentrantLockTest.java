package com.zcc.redis.lock;

import com.zcc.redis.RedisdemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisdemoApplication.class)
public class JedisWithReentrantLockTest {
    @Autowired
    JedisPool jedisPool;

    @Test
    public void test() {
        JedisWithReentrantLock redis = new JedisWithReentrantLock(jedisPool);
        System.out.println(redis.lock("zcc"));
        System.out.println(redis.lock("zcc"));
        System.out.println(redis.unlock("zcc"));
        System.out.println(redis.unlock("zcc"));

        jedisPool.getResource().set("zdd", "1");
    }

}