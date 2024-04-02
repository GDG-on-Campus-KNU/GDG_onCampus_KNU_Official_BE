package com.gdsc_knu.official_homepage.oauth;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String login;
    private String profileUrl;
    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String login, String profileUrl){
        this.attributes = attributes;
        this.login = login;
        this.profileUrl = profileUrl;
    }

    public static OAuthAttributes of(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .login((String) attributes.get("login"))
                .profileUrl((String) attributes.get("profileUrl"))
                .attributes(attributes)
                .build();
    }

    public Member toMember(){
        return Member.builder()
                .email(login)
                .profileUrl(profileUrl)
                .name("suhyeon")
                .age(24)
                .studentNumber("2021")
                .major("컴학")
                .role(Role.MEMBER)
                .build();
    }
}
