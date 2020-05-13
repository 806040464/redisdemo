package com.zcc.redis.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcc.redis.RedisdemoApplication;
import com.zcc.redis.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisdemoApplication.class)
public class RedisServiceImplTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void put() {
        User user1 = new User(1L, "aaa", "AAA");
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplate);
        redisService.put("user", "id", user1.getId());
        redisService.put("user", "name", user1.getName());
        redisService.put("user", "addr", user1.getAddr());
    }

    @Test
    public void putAll() {
        User user2 = new User(2L, "bbb", "BBB");
        User user3 = new User(3L, "ccc", "CCC");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", user2.getId());
        map1.put("name", user2.getName());
        map1.put("addr", user2.getAddr());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", user3.getId());
        map2.put("name", user3.getName());
        map2.put("addr", user3.getAddr());
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplate);
        redisService.putAll("user1", map1);
        redisService.putAll("user2", map2);
    }

    @Test
    public void get() {
        User user1 = new User(1L, "aaa", "AAA");
        User user2 = new User(2L, "bbb", "BBB");
        User user3 = new User(3L, "ccc", "CCC");
        List<User> users = Arrays.asList(user1, user2, user3);
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplate);
        redisService.put("user4", "list", users);
        List<User> o = (List<User>) redisService.get("user4", "list");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllUser() {
        List<String> list = Arrays.asList("id", "name", "addr");
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisTemplate(redisTemplate);
        List<User> user = redisService.getAllUser("user", list);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String value = objectMapper.writeValueAsString(user);
            System.out.println(value);
        } catch (JsonProcessingException e) {
            System.out.println("程序出错了！");
            e.printStackTrace();
        }
    }
}