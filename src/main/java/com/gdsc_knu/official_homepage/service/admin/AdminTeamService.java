package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.*;

import java.util.List;

public interface AdminTeamService {
    List<AdminTeamResponse.Team> getTeamInfos();

    Long createParentTeam(AdminTeamRequest.Create createRequest);

    Long createSubTeam(Long parentTeamId);

    List<AdminTeamResponse.TeamMember> getTeamMembers(Long teamId);

    Long changeTeamMember(AdminTeamRequest.Update updateRequest);

    void deleteParentTeam(Long ParentTeamId);

    void deleteSubTeam(Long subTeamId);
}