package com.example.demosignapp.interfaces.controller;

import com.example.demosignapp.application.AuthService;
import com.example.demosignapp.application.auth.LogoutResult;
import com.example.demosignapp.application.auth.ReissueTokenResult;
import com.example.demosignapp.interfaces.dto.*;
import com.example.demosignapp.interfaces.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller는 "DTO ↔ Mapper ↔ (Application)Command/Result"만 처리
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    /**
     * testUrl: http://localhost:8080/oauth2/authorization/kakao
     * Redis 하나 실행: docker run -d -p 6379:6379 --name my-redis redis:latest
     * 소셜 로그인 성공 후 (OAuth2SuccessHandler) -> /auth/success?accessToken=xxx
     */
    @GetMapping("/success")
    public ResponseEntity<String> authSuccess(@RequestParam(required = false) String accessToken) {
        // 기존 로직 유지
        return ResponseEntity.ok("소셜 로그인 성공! 토큰: " + accessToken);
    }

    /**
     * 만료된 AccessToken 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(@Valid @RequestBody ReissueRequestDto dto) {
        // 1) DTO → Command
        var command = authMapper.toCommand(dto);

        // 2) Application 호출
        ReissueTokenResult result = authService.reissueToken(command);

        // 3) Result → DTO
        var responseDto = authMapper.toDto(result);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 로그아웃(= 사용자의 모든 토큰 무효화)
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@Valid @RequestBody LogoutRequestDto dto) {
        // 1) DTO → Command
        var command = authMapper.toCommandForLogout(dto);

        // 2) Application 호출
        LogoutResult result = authService.logout(command);

        // 3) Result → DTO
        var responseDto = authMapper.toDto(result);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 특정 AccessToken만 무효화
     */
    @PostMapping("/logout/single")
    public ResponseEntity<LogoutResponseDto> logoutSingle(@Valid @RequestBody LogoutRequestDto dto) {
        var command = authMapper.toCommandForLogoutSingle(dto);
        LogoutResult result = authService.logoutSingleToken(command);
        var responseDto = authMapper.toDto(result);

        return ResponseEntity.ok(responseDto);
    }
}
