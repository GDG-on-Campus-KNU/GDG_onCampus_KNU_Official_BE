package com.gdsc_knu.official_homepage.dto.oauth;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GoogleUserInfo {
    private String sub;
    private String email;
    private Boolean email_verified;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
    private String hd;
    public Member toMember(){
        return Member.builder()
                .email(email)
                .profileUrl(picture)
                .name(name)
                .role(Role.ROLE_TEMP)
                .track(Track.UNDEFINED)
                .build();
    }
}
