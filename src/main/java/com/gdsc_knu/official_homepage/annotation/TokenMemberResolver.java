package com.gdsc_knu.official_homepage.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.JwtTokenValidator;
import com.gdsc_knu.official_homepage.authentication.redis.JwtMemberDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenMemberResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenMember.class)
                && parameter.getParameterType().equals(JwtMemberDetail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");

        String jwtToken = jwtTokenValidator.checkAccessToken(token);

        Claims claims = jwtTokenValidator.extractClaims(jwtToken);

        String email = claims.getSubject();

        return JwtMemberDetail.builder()
                .email(email)
                .build();
    }
}