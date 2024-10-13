package com.gdsc_knu.official_homepage.dto.post;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class PostRequest {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Create {
        private String title;
        private String subTitle;
        private String content;
        private String thumbnailUrl;
        private Category category;
        private PostStatus status;

        public static Post toEntity(Create create, Member member){
            LocalDateTime now = LocalDateTime.now();
            return Post.builder()
                    .title(create.getTitle())
                    .subTitle(create.getSubTitle())
                    .content(create.getContent())
                    .thumbnailUrl(create.getThumbnailUrl())
                    .category(create.getCategory())
                    .status(create.getStatus())
                    .member(member)
                    .publishedAt(now)
                    .modifiedAt(now)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Update {
        private String title;
        private String subTitle;
        private String content;
        private String thumbnailUrl;
        private Category category;
        private PostStatus status;
    }
}
