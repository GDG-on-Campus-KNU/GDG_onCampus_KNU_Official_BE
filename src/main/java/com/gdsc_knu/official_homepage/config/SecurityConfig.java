package com.gdsc_knu.official_homepage.config;

import com.gdsc_knu.official_homepage.authentication.JwtFilter;
import com.gdsc_knu.official_homepage.authentication.JwtTokenValidator;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenValidator jwtTokenValidator;
    private final MemberRepository memberRepository;

    private static final String[] WHITE_LIST = {
            "/**",
    };
    private static final String[] MEMBER_AUTHENTICATION_LIST = {
            "/api/jwt/member"
    };

    private static final String[] CORE_AUTHENTICATION_LIST = {
            "/api/jwt/core"
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(jwtTokenValidator,memberRepository), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers(MEMBER_AUTHENTICATION_LIST).hasRole("MEMBER")
                        .requestMatchers(CORE_AUTHENTICATION_LIST).hasRole("CORE")
                        .anyRequest().permitAll()
                );

        return httpSecurity.build();
    }
}