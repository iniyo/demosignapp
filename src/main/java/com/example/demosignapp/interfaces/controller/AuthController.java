package com.example.demosignapp.interfaces.controller;

import com.example.demosignapp.application.AuthService;
import com.example.demosignapp.application.auth.LogoutResult;
import com.example.demosignapp.application.auth.ReissueTokenResult;
import com.example.demosignapp.infrastructure.jwt.TokenProvider;
import com.example.demosignapp.infrastructure.util.AuthorizationHeaderUtil;
import com.example.demosignapp.interfaces.dto.*;
import com.example.demosignapp.interfaces.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
     * AccessToken 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        // Authorization 헤더에서 Bearer 토큰 추출
        String oldAccessToken = AuthorizationHeaderUtil.extractBearerToken(authorizationHeader);

        // AuthService 호출
        ReissueTokenResult result = authService.reissueToken(oldAccessToken);

        // 결과를 DTO로 변환하여 반환
        return ResponseEntity.ok(authMapper.toDto(result));
    }

    /**
     * 모든 토큰 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        String accessToken = AuthorizationHeaderUtil.extractBearerToken(authorizationHeader);

        // AuthService 호출
        LogoutResult result = authService.logout(accessToken);

        // 결과를 DTO로 변환하여 반환
        return ResponseEntity.ok(authMapper.toDto(result));
    }

    /**
     * 특정 AccessToken만 로그아웃
     */
    @PostMapping("/logout/single")
    public ResponseEntity<LogoutResponseDto> logoutSingle(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        String accessToken = AuthorizationHeaderUtil.extractBearerToken(authorizationHeader);

        // AuthService 호출
        LogoutResult result = authService.logoutSingle(accessToken);

        // 결과를 DTO로 변환하여 반환
        return ResponseEntity.ok(authMapper.toDto(result));
    }
}
