package com.gdsc_knu.official_homepage.authentication.jwt;

import com.gdsc_knu.official_homepage.authentication.redis.RedisRepository;
import com.gdsc_knu.official_homepage.authentication.redis.RedisToken;
import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final long jwtAccessExpiration = 1000 * 60 * 60 * 24; // 1일
    private final long jwtRefreshExpiration = 1000 * 60 * 60 * 24 * 14; // 1주

    private final RedisRepository redisRepository;
    private final JwtValidator jwtValidator;

    // 리프레쉬 토큰을 받아 검증하여 토큰을 재발급 할때 사용하는 메서드입니다.
    public TokenResponse reissueTokens(String token) {
        JwtClaims jwtClaims = jwtValidator.checkRefreshToken(token);
        return issueTokens(jwtClaims.getId(), jwtClaims.getEmail(), jwtClaims.getRole());
    }

    // 로그인 시 토큰을 발급할때 사용하는 메서드입니다.
    public TokenResponse issueTokens(Long id, String email, Role role) {
        long current = System.currentTimeMillis();
        Date accessTokenExpireTime = new Date(current + jwtAccessExpiration);
        Date refreshTokenExpireTime = new Date(current + jwtRefreshExpiration);

        Map<String, Object> claims = new HashMap<>();
        JwtClaims jwtClaims = JwtClaims.builder()
                .id(id)
                .email(email)
                .role(role)
                .build();
        claims.put("jwtClaims", jwtClaims);

        String accessToken = generateToken(accessTokenExpireTime, claims);
        String refreshToken = generateToken(refreshTokenExpireTime, claims);

        saveRefreshToken(email, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 발급을 위해 토큰을 생성하여 반환해주는 메서드입니다.
    private String generateToken(Date expiration, Map<String,?> claims) {
        Key secretKey = jwtValidator.createSignature();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰을 레디스에 저장하는 메서드입니다.
    private void saveRefreshToken(String email, String refreshToken) {
        RedisToken redisToken = RedisToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .build();
        redisRepository.save(redisToken);
    }
}