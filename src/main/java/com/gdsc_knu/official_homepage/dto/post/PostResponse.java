package com.gdsc_knu.official_homepage.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
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
        private String summary;
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
            int length = Math.min(post.getContent().length(), 20);
            return Main.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .summary(post.getContent().substring(0, length))
                    .thumbnailUrl(post.getThumbnailUrl())
                    .category(post.getCategory().name())
                    .createAt(post.getPublishedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .sharedCount(post.getSharedCount())
                    .build();

        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Detail {
        private Long id;
        private String title;
        private String summary;
        private String thumbnailUrl;
        private String category;
        private String content;
        private String authorName;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createAt;
        private int likeCount;
        private int commentCount;
        private int sharedCount;
        private boolean canDelete;
        private boolean canModify;

        public static Detail from(Post post, AccessModel access) {
            int length = Math.min(post.getContent().length(), 20);
            return Detail.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .summary(post.getContent().substring(0, length))
                    .thumbnailUrl(post.getThumbnailUrl())
                    .category(post.getCategory().name())
                    .content(post.getContent())
                    .authorName(post.getMember().getName())
                    .createAt(post.getPublishedAt())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .sharedCount(post.getSharedCount())
                    .canDelete(access.canDelete())
                    .canModify(access.canModify())
                    .build();

        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Temp {
        private Long id;
        private String title;
        private String summary;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createAt;

        public static Temp from(Post post) {
            int length = Math.min(post.getContent().length(), 20);
            return Temp.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .summary(post.getContent().substring(0, length))
                    .createAt(post.getPublishedAt())
                    .build();
        }
    }

//    @Getter
//    @Builder
//    @AllArgsConstructor
//    public static class Modify {
//        private String title;
//        private String content;
//        private String summary;
//        private String thumbnailUrl;
//        private Category category;
//        private PostStatus status;
//
//        public static Modify from(Post post) {
//            int length = Math.min(post.getContent().length(), 20);
//            return Modify.builder()
//                    .title(post.getTitle())
//                    .summary(post.getContent().substring(0, length))
//                    .content(post.getContent())
//                    .thumbnailUrl(post.getThumbnailUrl())
//                    .category(post.getCategory())
//                    .status(post.getStatus())
//                    .build();
//        }
//    }
}
