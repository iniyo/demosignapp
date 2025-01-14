package com.example.demosignapp.interfaces.mapper;


import com.example.demosignapp.application.auth.*;
import com.example.demosignapp.interfaces.dto.*;
import org.springframework.stereotype.Component;

/**
 * Controller의 DTO ↔ Application Command/Response 변환
 */
@Component
public class AuthMapper {

    // ========== Reissue ==========
    public ReissueTokenCommand toCommand(ReissueRequestDto dto) {
        return ReissueTokenCommand.builder()
                .oldAccessToken(dto.getOldAccessToken())
                .build();
    }

    public ReissueResponseDto toDto(ReissueTokenResult result) {
        return ReissueResponseDto.builder()
                .newAccessToken(result.getNewAccessToken())
                .message(result.getMessage())
                .build();
    }

    // ========== Logout(전체) ==========
    public LogoutCommand toCommandForLogout(LogoutRequestDto dto) {
        return LogoutCommand.builder()
                .memberKey(dto.getMemberKey())
                .build();
    }

    // ========== Logout(단일 토큰) ==========
    public LogoutSingleCommand toCommandForLogoutSingle(LogoutRequestDto dto) {
        return LogoutSingleCommand.builder()
                .accessToken(dto.getAccessToken())
                .build();
    }

    public LogoutResponseDto toDto(LogoutResult result) {
        return LogoutResponseDto.builder()
                .message(result.getMessage())
                .invalidatedToken(result.getInvalidatedToken())
                .build();
    }
}
