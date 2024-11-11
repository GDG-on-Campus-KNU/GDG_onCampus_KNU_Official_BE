package com.gdsc_knu.official_homepage.exception;

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
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordClient {
    @Value("${logging.discord.webhook-url}")
    private String webhookUrl;

    // TODO: 비동기 처리
    public void sendErrorAlert(Exception e, String message, HttpStatus status, HttpServletRequest request) {
        DiscordMessage discordMessage = createMessage(e, message, status, request);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(webhookUrl, discordMessage, String.class);
    }

    public void sendAttendance(List<String> names, String track) {
        DiscordMessage discordMessage = createAttendanceMessage(names, track);
        RestTemplate restTemplate = new RestTemplate();
        System.out.print(discordMessage.toString());
        restTemplate.postForObject(webhookUrl, discordMessage, String.class);
    }

    private DiscordMessage createAttendanceMessage(List<String> names, String track) {
        String nameList = "";
        for (String name : names) {
            nameList += name+" ";
        }
        LocalDate date = LocalDate.now();
        DiscordMessage.Embed embed = DiscordMessage.Embed.builder()
                .description(
                        date +"\n"+
                        track +"\n"+
                        nameList
                )
                .build();
        return DiscordMessage.builder()
                .content("## 세미나 불참자")
                .embeds(List.of(embed))
                .build();
    }

    private DiscordMessage createMessage(Exception e, String message, HttpStatus status, HttpServletRequest request) {
        DiscordMessage.Embed embed = DiscordMessage.Embed.builder()
                .description(
                        "❗**사용자 정보** \n" + getUserToken(request) +
                        "\n\n" +
                        "❗**요청 API** \n" + getRequestAPI(request) +
                        "\n\n" +
                        "❗**상태 코드** \n" + status +
                        "\n\n" +
                        "❗**오류 메세지** \n" + getStackTrace(e)
                        )
                .build();

        DiscordMessage response = DiscordMessage.builder()
                .content("## ⚠오류 발생")
                .embeds(List.of(embed))
                .build();

        return response;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString().substring(0,300);
    }

    private String getRequestAPI(HttpServletRequest request) {
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        return queryString != null
                ? fullPath + "?" + queryString
                : fullPath;
    }



    private String getUserToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null) {
            return "사용자 토큰이 없음";
        }
        try {
            String[] split_string = token.split("\\.");
            String base64EncodedBody = split_string[1];
            String body = new String(Base64.getDecoder().decode(base64EncodedBody));
            return body.substring(13);
        } catch (Exception e) {
            return "사용자 정보 가져오는데 실패함";
        }
    }

    private String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null
                ? "User-Agent: " + userAgent
                : "";
    }
}
