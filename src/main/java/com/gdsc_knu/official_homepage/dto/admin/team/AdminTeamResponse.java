package com.gdsc_knu.official_homepage.dto.admin.team;

import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


public class AdminTeamResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Team {
        private Long id;
        private String teamName;
        @Builder.Default
        private List<TeamResponse.Main> subTeams = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamMember {
        private Long id;
        private String name;
        private String studentNumber;
        private String profileUrl;
    }
}
