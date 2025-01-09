package com.example.demosignapp.interfaces.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequestDto {

    // 전체 로그아웃 시 필요한 memberKey
    private String memberKey;

    // 특정 토큰만 로그아웃 시 필요한 accessToken
    private String accessToken;

    // 필요하다면 @NotBlank 등으로 Validation 추가
}
