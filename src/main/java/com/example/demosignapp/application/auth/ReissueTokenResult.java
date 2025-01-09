package com.example.demosignapp.application.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 재발급 결과(새로운 AccessToken, 메시지 등)
 */
@Getter
@Builder
public class ReissueTokenResult {
    private final String newAccessToken;
    private final String message;
}