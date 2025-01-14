package com.example.demosignapp.application.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 특정 토큰만 로그아웃 Command
 */
@Getter
@Builder
public class LogoutSingleCommand {
    private final String accessToken;
}