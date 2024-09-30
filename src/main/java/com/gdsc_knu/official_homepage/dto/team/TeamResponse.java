package com.gdsc_knu.official_homepage.dto.team;

import com.gdsc_knu.official_homepage.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;

public class TeamResponse {
    @Builder
    @AllArgsConstructor
    public static class Main {
        private final Long id;
        private final String teamName;
        public static Main from(Team team) {
            return Main.builder()
                    .id(team.getId())
                    .teamName(team.getTeamName())
                    .build();
        }
    }
}
