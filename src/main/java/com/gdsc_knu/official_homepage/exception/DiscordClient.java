package com.gdsc_knu.official_homepage.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtClaims;
import com.gdsc_knu.official_homepage.dto.DiscordMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordClient {
    @Value("${logging.discord.webhook-url}")
    private String webhookUrl;
    private final ObjectMapper objectMapper;

    public void sendErrorAlert(Exception e, String message, HttpStatus status, HttpServletRequest request) {
        DiscordMessage discordMessage = createMessage(e, message, status, request);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(webhookUrl, discordMessage, String.class);
    }

    private DiscordMessage createMessage(Exception e, String message, HttpStatus status, HttpServletRequest request) {
        DiscordMessage.Embed embed = DiscordMessage.Embed.builder()
                .title("⚠ 오류 정보")
                .description("❗**발생 시간** \n" + LocalDateTime.now() +
                        "\n\n" +
                        "❗**http request** \n" + extractHttpRequest(request) +
                        "\n\n" +
                        "❗**상태 코드** \n" + status +
                        "\n\n" +
                        "❗**오류 메세지** \n" + "[" + e.getClass().getName() + "]: " + message +
                        "\n\n" +
                        "❗**추가 정보** \n" + getStackTrace(e)
                        )
                .build();

        DiscordMessage response = DiscordMessage.builder()
                .content("## 오류 발생")
                .embeds(List.of(embed))
                .build();

        return response;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString().substring(0,800);
    }

    private String extractHttpRequest(HttpServletRequest request) {
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        if (request.getHeader("Authorization") != null) {
            return fullPath + "\n사용자 이메일: " + parseUser(request.getHeader("Authorization"));
        }

        return fullPath;
    }


    private String parseUser(String token) {
        try {
            String[] split_string = token.split("\\.");
            String base64EncodedBody = split_string[1];
            String body = new String(Base64.getDecoder().decode(base64EncodedBody));
            JwtClaims jwtClaims = objectMapper.readValue(body, JwtClaims.class);
            return jwtClaims.getEmail();

        } catch (Exception e) {
            return "사용자 정보를 찾을 수 없습니다.";
        }
    }
}
