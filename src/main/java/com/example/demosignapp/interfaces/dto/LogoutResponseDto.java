package com.example.demosignapp.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponseDto {
    private String message;
    private String invalidatedToken;
}
