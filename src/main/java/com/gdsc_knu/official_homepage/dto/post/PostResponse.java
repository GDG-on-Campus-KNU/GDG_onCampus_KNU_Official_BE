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
    public static class Main implements Serializable {
        private Long id;
        private String title;
        private String subTitle;
        private String thumbnailUrl;
        private String category;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createAt;
        private int likeCount;
        private int commentCount;
        private int sharedCount;

        public static Main from(Post post) {
            return Main.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .subTitle(post.getSubTitle())
                    .thumbnailUrl(post.getThumbnailUrl())
                    .category(post.getCategory().name())
                    .createAt(post.getPublishedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentList().size())
                    .sharedCount(post.getSharedCount())
                    .build();

        }
    }
}
