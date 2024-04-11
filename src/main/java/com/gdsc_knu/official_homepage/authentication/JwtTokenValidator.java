package com.gdsc_knu.official_homepage.authentication;

import com.gdsc_knu.official_homepage.authentication.redis.RedisRepository;
import com.gdsc_knu.official_homepage.authentication.redis.RedisToken;
import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RedisRepository redisRepository;

    private String checkToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 형식의 토큰입니다.");
        }
        // Bearer 제거
        return token.substring(7);
    }

    public String checkAccessToken(String token) {
        return checkToken(token);
    }

    public String checkRefreshToken(String token) {
        String checkedToken = checkToken(token);
        JwtUserDetails userDetails = getUserDetails(checkedToken);
        RedisToken redisToken = redisRepository.findById(userDetails.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        if (!redisToken.getRefreshToken().equals(checkedToken)) {
            redisRepository.delete(redisToken);
            throw new IllegalArgumentException("요청한 리프레시 토큰이 저장된 토큰과 다릅니다.");
        }
        redisRepository.delete(redisToken);
        return userDetails.getEmail();
    }

    public JwtUserDetails getUserDetails(String token) {
        JwtUserDetails userDetails = null;
        try {
            Key key = createSignature();
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String email = body.get("email", String.class);
            String role = body.get("role", String.class);

            userDetails = new JwtUserDetails(email, role);

        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        return userDetails;
    }

    protected Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class JwtUserDetails {
        private String email;
        private String role;
    }
}
