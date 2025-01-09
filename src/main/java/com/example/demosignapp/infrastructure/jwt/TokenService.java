package com.example.demosignapp.infrastructure.jwt;

import com.example.demosignapp.domain.token.RefreshToken;
import com.example.demosignapp.domain.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdate(String memberKey, String accessToken, String refreshToken) {
        RefreshToken rt = RefreshToken.builder()
                .memberKey(memberKey)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.saveOrUpdate(rt);
    }

    @Transactional
    public void invalidateUserTokens(String memberKey) {
        refreshTokenRepository.deleteAllByMemberKey(memberKey);
    }

    @Transactional
    public void invalidateAccessToken(String accessToken) {
        refreshTokenRepository.deleteByAccessToken(accessToken);
    }
}
