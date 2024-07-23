package com.gdsc_knu.official_homepage.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class MemberRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Append {
        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "나이를 입력해주세요.")
        private int age;

        @NotBlank(message = "학번을 입력해주세요.")
        private String studentNumber;

        @NotBlank(message = "전공을 입력해주세요.")
        private String major;

        @NotBlank(message = "전화번호를 입력해주세요.")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        @NotNull(message = "프로필사진을 입력해주세요.")
        private MultipartFile profileUrl;

        @NotNull(message = "나이를 입력해주세요.")
        private int age;

        @NotBlank(message = "전공을 입력해주세요.")
        private String major;

        @NotBlank(message = "학번을 입력해주세요.")
        private String studentNumber;

        @NotBlank(message = "전화번호을 입력해주세요.")
        private String phoneNumber;

        private String introduction;
    }

}
