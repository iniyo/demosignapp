package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisClusterTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisClusterSetAndGet() {
        // 데이터 저장
        redisTemplate.opsForValue().set("testKey", "testValue");

        // 데이터 확인
        String value = (String) redisTemplate.opsForValue().get("testKey");
        assertThat(value).isEqualTo("testValue");
    }
}