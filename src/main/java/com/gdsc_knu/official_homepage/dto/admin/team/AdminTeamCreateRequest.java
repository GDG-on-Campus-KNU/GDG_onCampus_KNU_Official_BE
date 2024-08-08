package com.gdsc_knu.official_homepage.dto.admin.team;

import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTeamCreateRequest {
    private String teamName;
    private Track track;
}
