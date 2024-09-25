package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;

import java.util.List;

public interface MemberInfoService {
    MemberResponse getMemberInfo(Long id);
    void addMemberInfo(Long id, MemberRequest.Append memberInfoAdd);
    List<TeamInfoResponse> getMemberTeamInfo(Long id);
}
