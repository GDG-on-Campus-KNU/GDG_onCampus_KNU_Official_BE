package com.gdsc_knu.official_homepage.authentication.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtMemberDetail {
    private String email;
}
