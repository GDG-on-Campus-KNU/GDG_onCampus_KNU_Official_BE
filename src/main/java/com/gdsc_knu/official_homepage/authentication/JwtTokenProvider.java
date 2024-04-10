package com.gdsc_knu.official_homepage.authentication;

import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final long jwtAccessExpiration = 1000 * 60 * 60 * 24; // 1일
    private final long jwtRefreshExpiration = 1000 * 60 * 60 * 24 * 7; // 1주

    private final MemberRepository memberRepository;

    public TokenResponse issueTokens(String email) {
        long current = System.currentTimeMillis();
        Date accessTokenExpireTime = new Date(current + jwtAccessExpiration);
        Date refreshTokenExpireTime = new Date(current + jwtRefreshExpiration);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String accessToken = generateToken(accessTokenExpireTime, createClaims(member));
        String refreshToken = generateToken(refreshTokenExpireTime, createClaims(member));

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateToken(Date expiration, Map<String, Object> claims) {
        Key secretKey = createSignature();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    private static Map<String, Object> createClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole().toString());
        return claims;
    }

    protected Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}