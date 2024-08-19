package com.gdsc_knu.official_homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
