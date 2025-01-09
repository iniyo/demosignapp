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
     * 만료된 AccessToken -> 새 AccessToken 재발급
     */
    @Transactional
    public ReissueTokenResult reissueToken(ReissueTokenCommand command) {
        String oldAccessToken = command.getOldAccessToken();
        String newToken = tokenProvider.reissueAccessToken(oldAccessToken);

        if (newToken == null) {
            // 재발급 실패
            return ReissueTokenResult.builder()
                    .newAccessToken(null)
                    .message("재발급 실패(RefreshToken 만료 또는 없음)")
                    .build();
        }

        return ReissueTokenResult.builder()
                .newAccessToken(newToken)
                .message("새로운 AccessToken 발급 성공")
                .build();
    }

    /**
     * 로그아웃(= 모든 토큰 무효화)
     */
    @Transactional
    public LogoutResult logout(LogoutCommand command) {
        tokenService.invalidateUserTokens(command.getMemberKey());
        return LogoutResult.builder()
                .message("로그아웃 완료(모든 토큰 무효화)")
                .invalidatedToken(null)
                .build();
    }

    /**
     * 특정 AccessToken만 무효화
     */
    @Transactional
    public LogoutResult logoutSingleToken(LogoutSingleCommand command) {
        tokenService.invalidateAccessToken(command.getAccessToken());
        return LogoutResult.builder()
                .message("해당 토큰만 무효화 완료")
                .invalidatedToken(command.getAccessToken())
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
