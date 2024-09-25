package com.gdsc_knu.official_homepage.dto.member;

import com.gdsc_knu.official_homepage.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamInfoResponse {
    private Long id;
    private String teamName;

    public TeamInfoResponse(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
    }

    public static TeamInfoResponse from(Team team) {
        return TeamInfoResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .build();
    }
}