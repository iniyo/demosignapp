package com.example.demosignapp.infrastructure.jwt;

import com.example.demosignapp.domain.token.RefreshToken;
import com.example.demosignapp.domain.token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.key}")
    private String secretKeyString;

    private SecretKey secretKey;

    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;        // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    private static final String KEY_ROLE = "role";

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * (1) memberKey로 AccessToken 생성
     */
    public String generateAccessToken(String memberKey) {
        return generateToken(memberKey, ACCESS_TOKEN_EXPIRE_TIME);
        // 예: Role 하드코딩 or 다른 방식
    }

    /**
     * (2) memberKey로 RefreshToken 생성
     */
    public String generateRefreshToken(String memberKey) {
        return generateToken(memberKey, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String generateToken(String subject, long expireTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .subject(subject)
                .claim(KEY_ROLE, "ROLE_USER")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    /**
     * (3) AccessToken 만료 시 RefreshToken 으로 재발급
     */
    public String reissueAccessToken(String oldAccessToken) {
        if (!StringUtils.hasText(oldAccessToken)) {
            return null;
        }

        // Redis에서 oldAccessToken으로 RefreshToken 조회
        var optionalRt = refreshTokenRepository.findByAccessToken(oldAccessToken);
        if (optionalRt.isEmpty()) {
            return null;
        }

        var rt = optionalRt.get();
        String refreshToken = rt.getRefreshToken();

        if (!validateToken(refreshToken)) {
            return null;
        }

        // refreshToken -> memberKey 추출
        Authentication auth = getAuthentication(refreshToken);
        String memberKey = auth.getName();
        // => subject=memberKey

        // 새 AccessToken 발급
        String newAccessToken = generateAccessToken(memberKey);

        // Redis 갱신 (동시로그인 1회 제한)
        var newRt = RefreshToken.builder()
                .memberKey(rt.getMemberKey())
                .accessToken(newAccessToken)
                .refreshToken(rt.getRefreshToken())
                .build();
        refreshTokenRepository.saveOrUpdate(newRt);

        return newAccessToken;
    }

    /**
     * (4) 토큰에서 인증 객체 추출
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String roles = (String) claims.get(KEY_ROLE);
        var authorities = Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    /**
     * (5) 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;
        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey) // 서명 검증을 위한 키 설정
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 토큰이 만료된 경우에도 클레임을 반환
        } catch (JwtException e) {
            throw new TokenException("JWT 파싱 또는 서명 검증 실패", e);
        }
    }

}
