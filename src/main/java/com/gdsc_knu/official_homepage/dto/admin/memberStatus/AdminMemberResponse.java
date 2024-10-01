package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberResponse {
    private Long id;
    private Track track;
    private String name;
    private String studentNumber;
    private String email;
    private String phoneNumber;
    private List<TeamResponse.Main> teams;
    private Role role;

    public static AdminMemberResponse from(Member member) {
        return AdminMemberResponse.builder()
                .id(member.getId())
                .track(member.getTrack())
                .name(member.getName())
                .studentNumber(member.getStudentNumber())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .teams(member.getTeams().stream()
                        .map(TeamResponse.Main::from)
                        .toList())
                .role(member.getRole())
                .build();
    }
}
