package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisClusterConfiguration(Arrays.asList(
                "redis-cluster-master-0.redis-cluster-headless.default.svc.cluster.local:6379",
                "redis-cluster-master-1.redis-cluster-headless.default.svc.cluster.local:6379",
                "redis-cluster-master-2.redis-cluster-headless.default.svc.cluster.local:6379",
                "redis-cluster-slave-0.redis-cluster-headless.default.svc.cluster.local:6379",
                "redis-cluster-slave-1.redis-cluster-headless.default.svc.cluster.local:6379",
                "redis-cluster-slave-2.redis-cluster-headless.default.svc.cluster.local:6379"
        )));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
