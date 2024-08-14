package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.AdminMemberDeleteRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.AdminMemberRoleUpdateRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.AdminMemberTrackUpdateRequest;

public interface AdminMemberStatusService {
    public PagingResponse<AdminMemberResponse> getAllMemberInfos(int page, int size);

    public void deleteMember(AdminMemberDeleteRequest adminMemberDeleteRequest);

    public Long updateMemberRole(AdminMemberRoleUpdateRequest adminMemberRoleUpdateRequest);

    public Long updateMemberTrack(AdminMemberTrackUpdateRequest adminMemberTrackUpdateRequest);

    PagingResponse<AdminMemberResponse> getMemberByName(String name, int page, int size);
}
