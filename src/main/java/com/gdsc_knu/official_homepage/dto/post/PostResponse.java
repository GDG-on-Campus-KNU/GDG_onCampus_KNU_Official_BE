package com.gdsc_knu.official_homepage.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gdsc_knu.official_homepage.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PostResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Main implements Serializable{
        private Long id;
        private String title;
        private String summary;
        private String thumbnailUrl;
        private String category;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createAt;
        private int likeCount;
        private int commentCount;

        public static Main from(Post post) {
            int length = Math.min(post.getContent().length(), 20);
            return Main.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .summary(post.getContent().substring(0,length))
                    .thumbnailUrl(post.getThumbnailUrl())
                    .category(post.getCategory().name())
                    .createAt(post.getPublishedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .build();

        }
    }
}