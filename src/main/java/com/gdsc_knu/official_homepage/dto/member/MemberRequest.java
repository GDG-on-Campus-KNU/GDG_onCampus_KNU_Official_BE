package com.gdsc_knu.official_homepage.dto.member;

import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Append {
        private String name;
        private int age;
        private String studentNumber;
        private String major;
        private String phoneNumber;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        private String name;
        private String profileUrl;
        private int age;
        private String major;
        private String studentNumber;
        private String email;
        private String phoneNumber;
        private String introduction;
    }

}
