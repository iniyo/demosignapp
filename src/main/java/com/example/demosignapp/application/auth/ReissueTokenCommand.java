package com.example.demosignapp.application.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 만료된 토큰 재발급을 위한 내부 Command
 */
@Getter
@Builder
public class ReissueTokenCommand {
    private final String oldAccessToken;
}