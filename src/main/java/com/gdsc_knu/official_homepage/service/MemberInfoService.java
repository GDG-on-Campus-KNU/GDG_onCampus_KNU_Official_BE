package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.Member.MemberInfoAdd;
import com.gdsc_knu.official_homepage.dto.Member.MemberInfoUpdate;
import com.gdsc_knu.official_homepage.dto.Member.MemberInfoResponse;

public interface MemberInfoService {
    MemberInfoResponse retrieveMemberInfo(Long id);
    void updateMemberInfo(Long id, MemberInfoUpdate memberInfoUpdate);
    void addMemberInfo(MemberInfoAdd memberInfoAdd);
}
