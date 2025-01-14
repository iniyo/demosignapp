package com.example.demosignapp.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
public class RedisClusterProperties {
    private String password;
    private int maxRedirects = 3; // 기본값 설정
    private List<String> nodes;
}