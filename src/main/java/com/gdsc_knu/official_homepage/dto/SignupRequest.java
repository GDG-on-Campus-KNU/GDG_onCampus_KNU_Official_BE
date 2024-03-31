package com.gdsc_knu.official_homepage.dto;

import com.gdsc_knu.official_homepage.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String name;
    private int age;
    private String studentNumber;
    private String major;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .studentNumber(studentNumber)
                .major(major)
                .email(email)
                .build();
    }
}
