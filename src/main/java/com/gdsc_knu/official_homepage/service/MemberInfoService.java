package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.MemberInfoRequest;
import com.gdsc_knu.official_homepage.dto.MemberInfoResponse;

public interface MemberInfoService {
    MemberInfoResponse retrieveMemberInfo(Long id);
    Long updateMemberInfo(Long id, MemberInfoRequest memberInfoRequest);
}
