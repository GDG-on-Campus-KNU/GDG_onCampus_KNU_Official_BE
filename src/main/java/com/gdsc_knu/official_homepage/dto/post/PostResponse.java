package com.gdsc_knu.official_homepage.dto.post;

import com.gdsc_knu.official_homepage.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Main {
        private Long id;
        private String title;
        private String summary;
        private String thumbnailUrl;
        private String category;
        private LocalDateTime createAt;
        private int likeCount;
        private int commentCount;

        public static Main from(Post post) {
            return Main.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .summary(post.getContent().substring(0,20))
                    .thumbnailUrl(post.getThumbnailUrl())
                    .category(post.getCategory().name())
                    .createAt(post.getPublishedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .build();

        }
    }
}
