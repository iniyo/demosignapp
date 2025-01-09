package com.example.demosignapp.infrastructure.config;

import com.example.demosignapp.infrastructure.oauth2.CustomOAuth2UserService;
import com.example.demosignapp.infrastructure.oauth2.OAuth2SuccessHandler;
import com.example.demosignapp.infrastructure.filter.TokenAuthenticationFilter;
import com.example.demosignapp.infrastructure.filter.TokenExceptionFilter;
import com.example.demosignapp.infrastructure.handler.CustomAccessDeniedHandler;
import com.example.demosignapp.infrastructure.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter; // 빈으로 관리되는 TokenAuthenticationFilter
    private final TokenExceptionFilter tokenExceptionFilter; // 빈으로 관리되는 TokenExceptionFilter

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )

                .authorizeHttpRequests(auth -> auth

                        // === Swagger UI, API Docs 허용 ===
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // === 기존 허용 경로 ===
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/auth/success",
                                "/auth/reissue",
                                "/auth/logout"
                        ).permitAll()

                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                );

        // === 필터 등록 순서 ===
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 인증 필터는 먼저 동작
        http.addFilterAfter(tokenExceptionFilter, TokenAuthenticationFilter.class); // 예외 필터는 인증 필터 이후에 동작

        return http.build();
    }
}
