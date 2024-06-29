package com.gdsc_knu.official_homepage.dto.oauth;

import lombok.Getter;

@Getter
public class AuthorizationCode {
    private String code;
    private Provider provider;
}
