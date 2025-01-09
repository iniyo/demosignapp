package com.example.demosignapp.application.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 로그아웃 결과
 */
@Getter
@Builder
public class LogoutResult {
    private final String message;
    private final String invalidatedToken; // 단일 토큰 로그아웃 시
}