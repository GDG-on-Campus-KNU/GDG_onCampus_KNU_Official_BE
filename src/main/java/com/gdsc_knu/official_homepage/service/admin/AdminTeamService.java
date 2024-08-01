package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.util.List;

public interface AdminTeamService {
    List<AdminTeamInfoResponse> getTeamInfos();

    Long createTeam(String teamName, Track track);

    Long createSubTeam(Long teamId);

    List<AdminMemberResponse> getTeamMembers(Long parentTeamId);

    Long changeTeamMember(Long teamId, Long memberId);
}