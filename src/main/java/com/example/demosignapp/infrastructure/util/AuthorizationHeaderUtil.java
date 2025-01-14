package com.example.demosignapp.infrastructure.util;

public class AuthorizationHeaderUtil {

    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     */
    public static String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }
}
