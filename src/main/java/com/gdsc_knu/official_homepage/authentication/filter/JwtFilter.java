package com.gdsc_knu.official_homepage.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtClaims;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtValidator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 인증이 필요한 모든 요청은 JwtFilter를 탐.
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private JwtValidator jwtValidator;
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtHeader = request.getHeader("Authorization");

        if (jwtHeader==null){
            request.setAttribute("exception", "엑세스 토큰이 존재하지 않습니다.");
            chain.doFilter(request,response);
            return;
        }

        try {
            String jwtToken = jwtValidator.checkAccessToken(jwtHeader);
            Claims claims = jwtValidator.extractClaims(jwtToken);

            JwtClaims jwtClaims = objectMapper.convertValue(claims.get("jwtClaims"), JwtClaims.class);

            JwtMemberDetail jwtMemberDetail = JwtMemberDetail.builder()
                    .id(jwtClaims.getId())
                    .email(jwtClaims.getEmail())
                    .role(jwtClaims.getRole())
                    .build();

            // jwt 서명이 정상이면 Authentication객체를 만듦.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtMemberDetail, null, jwtMemberDetail.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (RuntimeException e) {
            request.setAttribute("exception", e.getMessage());
        }

        chain.doFilter(request,response);

    }
}