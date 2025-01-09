package com.example.demosignapp.application.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * 전체 로그아웃 Command
 */
@Getter
@Builder
public class LogoutCommand {
    private final String memberKey;
}