package com.gdsc_knu.official_homepage.dto;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    @NotBlank
    private String name;
    @NotNull
    private int age;
    @NotBlank
    private String studentNumber;
    @NotBlank
    private String major;
    @NotBlank
    @Email
    private String email;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .studentNumber(studentNumber)
                .major(major)
                .email(email)
                .role(Role.MEMBER)
                .build();
    }
}
