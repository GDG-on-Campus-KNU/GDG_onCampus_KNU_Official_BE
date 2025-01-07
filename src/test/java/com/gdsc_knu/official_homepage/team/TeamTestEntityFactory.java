package com.gdsc_knu.official_homepage.team;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

public class TeamTestEntityFactory {
    public static Team createTeam(Long id, Team parent) {
        return Team.builder()
                .id(id)
                .teamName("팀 이름")
                .parent(parent)
                .build();
    }

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
}
