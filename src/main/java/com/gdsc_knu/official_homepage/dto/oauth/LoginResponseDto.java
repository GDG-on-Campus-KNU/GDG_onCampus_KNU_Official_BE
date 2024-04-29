package com.gdsc_knu.official_homepage.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponseDto {
    private Long id;
    private boolean isNewMember;
    private String accessToken;
    private String refreshToken;
}
