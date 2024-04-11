package com.gdsc_knu.official_homepage.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoUpdate {
    private String name;
    private String profileUrl;
    private int age;
    private String major;
    private String studentNumber;
    private String email;
    private String introduction;
}
