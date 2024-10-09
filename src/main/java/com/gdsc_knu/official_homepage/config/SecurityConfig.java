package com.gdsc_knu.official_homepage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.exception.JwtAccessDeniedHandler;
import com.gdsc_knu.official_homepage.authentication.exception.JwtAuthenticationEntryPoint;
import com.gdsc_knu.official_homepage.authentication.filter.JwtFilter;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtValidator jwtValidator;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ObjectMapper objectMapper;


    private static final String[] WHITE_LIST = {
            "/api/post/{postId:\\d+}/comment/**",
            "/api/post/trending"
    };
    private static final String[] MEMBER_AUTHENTICATION_LIST = {

    };

    private static final String[] CORE_AUTHENTICATION_LIST = {
            "/api/admin/**"
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        httpSecurity
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(jwtValidator, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers(CORE_AUTHENTICATION_LIST).hasRole("CORE")
                        .requestMatchers(HttpMethod.GET, WHITE_LIST).permitAll()
                        .anyRequest().permitAll()
                );

        return httpSecurity.build();
    }
}
