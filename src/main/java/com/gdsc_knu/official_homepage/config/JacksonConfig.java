package com.gdsc_knu.official_homepage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig { // ObjectMapper를 Bean으로 등록해 중복 생성 비용을 줄이기 위한 설정

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
