package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamUpdateRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamCreateRequest;

import java.util.List;

public interface AdminTeamService {
    List<AdminTeamResponse> getTeamInfos();

    Long createTeam(AdminTeamCreateRequest adminTeamCreateRequest);

    Long createSubTeam(Long parentTeamId);

    List<AdminTeamMemberResponse> getTeamMembers(Long teamId);

    Long changeTeamMember(AdminTeamUpdateRequest adminTeamUpdateRequest);
}