package com.gdsc_knu.official_homepage.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiscordMessage {
    private String content;
    private boolean tts;
    private List<Embed> embeds;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Embed {
        private String title;
        private String description;
    }

    @Builder
    public DiscordMessage(String content, List<Embed> embeds) {
        this.content = content;
        this.embeds = embeds;
        this.tts = false;
    }

    public static DiscordMessage createAttendanceMessage(List<String> names, String track) {
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

    public static DiscordMessage createMessage(Exception e, HttpStatus status, HttpServletRequest request) {
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

        return DiscordMessage.builder()
                .content("## ⚠오류 발생")
                .embeds(List.of(embed))
                .build();
    }

    private static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString().substring(0,300);
    }

    private static String getRequestAPI(HttpServletRequest request) {
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        return queryString != null
                ? fullPath + "?" + queryString
                : fullPath;
    }



    private static String getUserToken(HttpServletRequest request) {
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
}
