package com.gdsc_knu.official_homepage.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private Long groupId;

        @NotBlank(message = "내용을 입력해주세요")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "내용을 입력해주세요")
        private String content;
    }
}
