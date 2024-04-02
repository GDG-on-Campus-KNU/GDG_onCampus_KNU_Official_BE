package com.gdsc_knu.official_homepage.dto;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequest {
    private String name;
    private String profileUrl;
    private int age;
    private String major;
    private String studentNumber;
    private String email;
    private String introduction;
}
