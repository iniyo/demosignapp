package com.example.demosignapp.infrastructure.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Configuration
public class RedisConfig {

    /**
     * application.yml (또는 properties) 에서 spring.redis.cluster.*
     * 관련 값들을 주입받는 클래스라 가정
     */
    private final RedisClusterProperties redisClusterProperties;

    public RedisConfig(RedisClusterProperties redisClusterProperties) {
        this.redisClusterProperties = redisClusterProperties;
    }

    /**
     * RedisClusterConfiguration 빈 등록
     * - nodes, maxRedirects 등 cluster 환경 구성
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration config = new RedisClusterConfiguration(redisClusterProperties.getNodes());
        // config.setMaxRedirects(redisClusterProperties.getMaxRedirects());
        if (StringUtils.hasText(redisClusterProperties.getPassword())) {
            config.setPassword(RedisPassword.of(redisClusterProperties.getPassword()));
        }
        return config;
    }

    /**
     * LettuceConnectionFactory 빈 등록
     * - ClientOptions나 timeout 등 세부 설정 가능
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            RedisClusterConfiguration redisClusterConfiguration) {

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(5))
                .clientOptions(ClientOptions.builder()
                        //.protocolVersion(ProtocolVersion.)
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.DEFAULT)
                        .autoReconnect(true)
                        .build())
                .build();

        return new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
    }


    /**
     * RedisTemplate<String, Object> 빈 등록
     * - 실제로 사용하는 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 직렬화 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
