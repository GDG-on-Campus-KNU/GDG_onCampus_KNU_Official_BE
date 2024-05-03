package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.MemberInfoAdd;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoUpdate;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoResponse;

public interface MemberInfoService {
    MemberInfoResponse retrieveMemberInfo(Long id);
    void updateMemberInfo(Long id, MemberInfoUpdate memberInfoUpdate);
    void addMemberInfo(Long id, MemberInfoAdd memberInfoAdd);
}
