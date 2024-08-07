package com.gdsc_knu.official_homepage.dto.admin.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTeamChangeRequest {
    private Long oldTeamId;
    private Long newTeamId;
    private Long memberId;
}
