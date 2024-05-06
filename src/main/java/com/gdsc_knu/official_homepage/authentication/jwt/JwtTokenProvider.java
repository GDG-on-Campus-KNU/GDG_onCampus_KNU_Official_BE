package com.gdsc_knu.official_homepage.authentication.jwt;

import com.gdsc_knu.official_homepage.authentication.redis.RedisRepository;
import com.gdsc_knu.official_homepage.authentication.redis.RedisToken;
import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final long jwtAccessExpiration = 1000 * 60 * 60 * 24; // 1일
    private final long jwtRefreshExpiration = 1000 * 60 * 60 * 24 * 14; // 1주

    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;
    private final JwtTokenValidator jwtTokenValidator;

    // 리프레쉬 토큰을 받아 검증하여 토큰을 재발급 할때 사용하는 메서드입니다.
    public TokenResponse reissueTokens(String token) {
        String email = jwtTokenValidator.checkRefreshToken(token);
        return issueTokens(email);
    }

    // 로그인 시 토큰을 발급할때 사용하는 메서드입니다.
    public TokenResponse issueTokens(String email) {
        long current = System.currentTimeMillis();
        Date accessTokenExpireTime = new Date(current + jwtAccessExpiration);
        Date refreshTokenExpireTime = new Date(current + jwtRefreshExpiration);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new JwtException("올바르지 않은 사용자 정보를 담은 토큰입니다."));

        String accessToken = generateToken(accessTokenExpireTime, email);
        String refreshToken = generateToken(refreshTokenExpireTime, email);

        saveRefreshToken(email, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 발급을 위해 토큰을 생성하여 반환해주는 메서드입니다.
    private String generateToken(Date expiration, String email) {
        Key secretKey = jwtTokenValidator.createSignature();

        return Jwts.builder()
                .setSubject(email)
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