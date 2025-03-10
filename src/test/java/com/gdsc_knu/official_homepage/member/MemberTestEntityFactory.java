package com.gdsc_knu.official_homepage.member;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

public class MemberTestEntityFactory {
    public static Member createMember(Long id) {
        return Member.builder()
                .id(id)
                .email(String.format("test%s@email.com", id))
                .name(String.format("test%s", id))
                .age((int) (20 + id % 5))
                .major(String.format("컴퓨터학부-%s", id))
                .studentNumber(String.format("2024%06d", id))
                .track(Track.UNDEFINED)
                .role(Role.ROLE_TEMP)
                .phoneNumber(String.format("010-0000-%04d", id))
                .profileUrl(String.format("www.test%s.com", id))
                .build();
    }

    public static Member createMember(Long id, String name, Track track, Role role) {
        return Member.builder()
                .id(id)
                .email(String.format("test%s@email.com", id))
                .name(name)
                .age((int) (20 + id % 5))
                .major(String.format("컴퓨터학부-%s", id))
                .studentNumber(String.format("2024%06d", id))
                .track(track)
                .role(role)
                .phoneNumber(String.format("010-0000-%04d", id))
                .profileUrl(String.format("www.test%s.com", id))
                .build();
    }
}
