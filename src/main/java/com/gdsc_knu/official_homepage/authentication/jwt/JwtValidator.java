package com.gdsc_knu.official_homepage.authentication.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.redis.RedisRepository;
import com.gdsc_knu.official_homepage.authentication.redis.RedisToken;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    // 토큰의 형식을 검사하는 private 메서드입니다.
    private String checkToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.JWT_NOT_FOUND);
        }
        // Bearer 제거
        return token.substring(7);
    }

    // 엑세스 토큰 검사하는 (위의 메서드와 동일) public 메서드입니다.
    public String checkAccessToken(String token) {
        return checkToken(token);
    }

    // 리프레쉬 토큰을 검사하는 메서드입니다.
    public JwtClaims checkRefreshToken(String token) {
        String refreshToken = checkToken(token);
        Claims claims = extractClaims(refreshToken);

        JwtClaims jwtClaims = objectMapper.convertValue(claims.get("jwtClaims"), JwtClaims.class);
        String email = jwtClaims.getEmail();

        RedisToken redisToken = redisRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.RT_NOT_FOUND));
        if (!redisToken.getEmail().equals(email)) {
            redisRepository.delete(redisToken);
            throw new CustomException(ErrorCode.RT_INCORRECT);
        }
        redisRepository.delete(redisToken);
        return jwtClaims;
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(createSignature())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.JWT_INVALID);
        }
    }
    protected Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
