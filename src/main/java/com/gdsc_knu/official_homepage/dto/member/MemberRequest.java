package com.gdsc_knu.official_homepage.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
