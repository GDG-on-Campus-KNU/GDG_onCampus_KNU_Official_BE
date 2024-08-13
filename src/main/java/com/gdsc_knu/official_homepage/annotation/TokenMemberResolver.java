package com.gdsc_knu.official_homepage.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtClaims;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtValidator;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import io.jsonwebtoken.Claims;
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
    private final JwtValidator jwtValidator;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenMember.class)
                && parameter.getParameterType().equals(JwtMemberDetail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");

        String jwtToken = jwtValidator.checkAccessToken(token);

        Claims claims = jwtValidator.extractClaims(jwtToken);
        ObjectMapper mapper = new ObjectMapper();
        JwtClaims jwtClaims = mapper.convertValue(claims.get("jwtClaims"), JwtClaims.class);

        String email = jwtClaims.getEmail();

        return JwtMemberDetail.builder()
                .email(email)
                .id(jwtClaims.getId())
                .role(jwtClaims.getRole())
                .build();
    }
}
