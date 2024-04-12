package com.gdsc_knu.official_homepage.dto.oauth;

import lombok.Getter;

@Getter
public class GoogleToken {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
