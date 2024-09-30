package com.gdsc_knu.official_homepage.dto.member;

import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberResponse {
    @Builder
    @AllArgsConstructor
    public static class Main {
        private String name;
        private String profileUrl;
        private int age;
        private String major;
        private String studentNumber;
        private String email;
        private Role role;
        private List<TeamResponse.Main> teamInfos;
        public static MemberResponse.Main from (Member member) {
            return Main.builder()
                    .name(member.getName())
                    .profileUrl(member.getProfileUrl())
                    .age(member.getAge())
                    .major(member.getMajor())
                    .studentNumber(member.getStudentNumber())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .teamInfos(member.getTeams().stream()
                            .map(TeamResponse.Main::from)
                            .toList())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    public static class WithTrack {
        private Long id;
        private String name;
        private String track;

        public static WithTrack from(Member member) {
            return WithTrack.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .track(member.getTrack().name())
                    .build();
        }
    }
}
