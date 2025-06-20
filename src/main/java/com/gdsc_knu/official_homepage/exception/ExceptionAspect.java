package com.gdsc_knu.official_homepage.exception;

import com.gdsc_knu.official_homepage.service.discord.DiscordClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionAspect {

    private final DiscordClient discordClient;
    private final Environment environment;

    @Pointcut("execution(* com.gdsc_knu.official_homepage.exception.GlobalExceptionHandler.*(..))")
    public void exceptionHandlerMethods() {}

    @AfterReturning(
            pointcut = "exceptionHandlerMethods()",
            returning = "response"
    )
    public void sendAlertAfterExceptionHandled(Object response) {
        if (!Arrays.asList(environment.getActiveProfiles()).contains("prod")) return;

        if (response instanceof ResponseEntity<?> entity) {
            Object body = entity.getBody();
            if (body instanceof ExceptionDto dto) {
                discordClient.sendErrorAlert(dto.getMessage(), entity.getStatusCode());
                log.error("[Discord Alert] {}", dto.message());
            }
        }
    }
}
