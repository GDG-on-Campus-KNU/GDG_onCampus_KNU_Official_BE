package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.util.List;

public interface MemberInfoService {
    MemberResponse.Main getMemberInfo(Long id);
    void addMemberInfo(Long id, MemberRequest.Append memberInfoAdd);
    List<TeamResponse.Main> getMemberTeamInfo(Long id);
    Member getMemberAdmin(String email, Track track, Role role);
}
