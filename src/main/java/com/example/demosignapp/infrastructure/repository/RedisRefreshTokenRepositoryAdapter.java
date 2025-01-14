package com.example.demosignapp.infrastructure.repository;

import com.example.demosignapp.domain.token.RefreshToken;
import com.example.demosignapp.domain.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
class RedisRefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "RT:";
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);

    @Override
    public void saveOrUpdate(RefreshToken token) {
        String key = PREFIX + token.getAccessToken();
        redisTemplate.opsForValue().set(key, token, REFRESH_TOKEN_TTL.toSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public Optional<RefreshToken> findByAccessToken(String accessToken) {
        String key = PREFIX + accessAccessToken(accessToken);
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            return Optional.of((RefreshToken) value);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAllByMemberKey(String memberKey) {
        redisTemplate.keys(PREFIX + "*").forEach(key -> {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null && memberKey.equals(((RefreshToken) value).getMemberKey())) {
                redisTemplate.delete(key);
            }
        });
    }

    @Override
    public void deleteByAccessToken(String accessToken) {
        String key = PREFIX + accessAccessToken(accessToken);
        redisTemplate.delete(key);
    }

    private String accessAccessToken(String accessToken) {
        return accessToken == null ? "" : accessToken;
    }
}
