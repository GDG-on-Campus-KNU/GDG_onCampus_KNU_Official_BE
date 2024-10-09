package com.gdsc_knu.official_homepage.dto.post;

import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
