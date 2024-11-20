package com.gdsc_knu.official_homepage.service.discord;

import com.gdsc_knu.official_homepage.dto.DiscordMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.gdsc_knu.official_homepage.dto.DiscordMessage.createAttendanceMessage;
import static com.gdsc_knu.official_homepage.dto.DiscordMessage.createMessage;

@Component
@RequiredArgsConstructor
public class DiscordClient {
    @Value("${logging.discord.webhook-url}")
    private String webhookUrl;

    @Value("${logging.discord.attendance-webhook-url}")
    private String attendanceWebhookUrl;

    public void sendErrorAlert(Exception e, HttpStatus status, HttpServletRequest request) {
        DiscordMessage discordMessage = createMessage(e, status, request);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(webhookUrl, discordMessage, String.class);
    }

    public void sendAttendance(List<String> names, String track) {
        DiscordMessage discordMessage = createAttendanceMessage(names, track);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(attendanceWebhookUrl, discordMessage, String.class);
    }

}
