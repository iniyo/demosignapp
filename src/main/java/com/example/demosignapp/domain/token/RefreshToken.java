package com.example.demosignapp.domain.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

/**
 * Redis에 저장할 RefreshToken 도메인 객체
 */
@Getter
public class RefreshToken {
    private final String memberKey;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public RefreshToken(String memberKey, String accessToken, String refreshToken) {
        this.memberKey = memberKey;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /**
     * 객체를 JSON 문자열로 변환
     */
    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert RefreshToken to JSON", e);
        }
    }

    /**
     * JSON 문자열을 객체로 변환
     */
    public static RefreshToken fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RefreshToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON to RefreshToken", e);
        }
    }
}
