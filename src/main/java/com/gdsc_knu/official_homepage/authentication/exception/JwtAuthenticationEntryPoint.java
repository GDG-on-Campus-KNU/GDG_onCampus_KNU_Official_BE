package com.gdsc_knu.official_homepage.authentication.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.exception.ExceptionDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        if (request.getAttribute("exception") != null) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            ObjectMapper objectMapper = new ObjectMapper();
            ExceptionDto exceptionDto = new ExceptionDto(ErrorCode.INVALID_PERMISSION, request.getAttribute("exception").toString());
            objectMapper.writeValue(response.getOutputStream(), exceptionDto);
        }

    }
}