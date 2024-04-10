package com.gdsc_knu.official_homepage.authentication;

import com.gdsc_knu.official_homepage.authentication.redis.RedisRepository;
import com.gdsc_knu.official_homepage.authentication.redis.RedisToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final JwtTokenProvider jwtTokenProvider;
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
        RedisToken redisToken = redisRepository.findById(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        if (!redisToken.getEmail().equals(getUserDetails(token).getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
        return checkedToken;
    }

    public JwtUserDetails getUserDetails(String token) {
        JwtUserDetails userDetails = null;
        try {
            Key key = jwtTokenProvider.createSignature();
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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class JwtUserDetails {
        private String email;
        private String role;
    }
}
