package com.example.demosignapp.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueRequestDto {

    @NotBlank(message = "기존 AccessToken은 필수입니다.")
    private String oldAccessToken;
}
