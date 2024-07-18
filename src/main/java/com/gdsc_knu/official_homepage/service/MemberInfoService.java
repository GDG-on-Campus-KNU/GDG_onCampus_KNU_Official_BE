package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;

public interface MemberInfoService {
    MemberResponse getMemberInfo(Long id);
    void updateMemberInfo(Long id, MemberRequest.Update memberInfoUpdate);
    void addMemberInfo(Long id, MemberRequest.Append memberInfoAdd);
}
