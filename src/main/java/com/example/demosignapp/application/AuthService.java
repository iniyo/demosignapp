package com.example.demosignapp.application;

import com.example.demosignapp.application.auth.*;
import com.example.demosignapp.infrastructure.jwt.TokenProvider;
import com.example.demosignapp.infrastructure.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

    /**
     * AccessToken 재발급
     */
    @Transactional
    public ReissueTokenResult reissueToken(String oldAccessToken) {
        String newAccessToken = tokenProvider.reissueAccessToken(oldAccessToken);

        return ReissueTokenResult.builder()
                .newAccessToken(newAccessToken)
                .message(newAccessToken == null ? "AccessToken이 성공적으로 재발급에 실패했습니다." : "AccessToken이 성공적으로 재발급되었습니다.")
                .build();
    }

    /**
     * 모든 토큰 로그아웃
     */
    @Transactional
    public LogoutResult logout(String accessToken) {
        String memberKey = tokenProvider.getAuthentication(accessToken).getName();
        tokenService.invalidateUserTokens(memberKey);

        return LogoutResult.builder()
                .message("모든 토큰이 무효화되었습니다.")
                .build();
    }

    /**
     * 특정 AccessToken 로그아웃
     */
    @Transactional
    public LogoutResult logoutSingle(String accessToken) {
        tokenService.invalidateAccessToken(accessToken);

        return LogoutResult.builder()
                .message("AccessToken이 무효화되었습니다.")
                .invalidatedToken(accessToken)
                .build();
    }

    /**
     * 소셜 로그인 성공 후, 토큰 발급(기존 예시)
     * - memberKey만 있으면 Access/Refresh Token 발급
     */
    @Transactional
    public String reissueTokensAfterOAuth2(String memberKey) {
        // 기존 예시 로직
        String newAccessToken = tokenProvider.generateAccessToken(memberKey);
        String newRefreshToken = tokenProvider.generateRefreshToken(memberKey);
        tokenService.saveOrUpdate(memberKey, newAccessToken, newRefreshToken);
        return newAccessToken;
    }
}
