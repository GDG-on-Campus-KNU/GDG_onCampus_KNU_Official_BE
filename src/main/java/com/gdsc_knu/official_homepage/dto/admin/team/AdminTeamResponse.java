package com.gdsc_knu.official_homepage.dto.admin.team;

import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTeamResponse {
    private Long id;
    private String teamName;
    private String teamPageUrl;
    private List<TeamInfoResponse> subTeams = new ArrayList<>();
}
