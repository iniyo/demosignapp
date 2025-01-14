package com.example.demosignapp.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String newAccessToken;
    private String message;
}
