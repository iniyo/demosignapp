package com.example.demosignapp.infrastructure.oauth2;

import com.example.demosignapp.application.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * 소셜 로그인 성공 시 호출되어 토큰 발급
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // memberKey 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // PrincipalDetails -> memberKey
        String memberKey = authentication.getName();

        // 토큰 발급
        String accessToken = authService.reissueTokensAfterOAuth2(memberKey);

        // 예: /auth/success?accessToken=xxxx 로 리다이렉트
        String redirectUrl = UriComponentsBuilder.fromUriString("/auth/success")
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
