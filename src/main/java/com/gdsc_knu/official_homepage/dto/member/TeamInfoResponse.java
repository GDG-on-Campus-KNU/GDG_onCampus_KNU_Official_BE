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
    private String teamPageUrl;

    public TeamInfoResponse(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.teamPageUrl = team.getTeamPageUrl();
    }
}