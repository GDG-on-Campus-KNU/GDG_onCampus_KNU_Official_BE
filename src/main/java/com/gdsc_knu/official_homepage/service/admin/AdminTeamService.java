package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamChangeRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamCreateRequest;

import java.util.List;

public interface AdminTeamService {
    List<AdminTeamResponse> getTeamInfos();

    Long createTeam(AdminTeamCreateRequest adminTeamCreateRequest);

    Long createSubTeam(Long parentTeamId);

    List<AdminMemberResponse> getTeamMembers(Long teamId);

    Long changeTeamMember(AdminTeamChangeRequest adminTeamChangeRequest);
}