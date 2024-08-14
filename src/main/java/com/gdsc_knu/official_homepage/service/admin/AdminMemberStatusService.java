package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.*;

public interface AdminMemberStatusService {
    public PagingResponse<AdminMemberResponse> getAllMemberInfos(int page, int size);

    public void deleteMember(AdminMemberRequest.Delete deleteRequest);

    public Long updateMemberRole(AdminMemberRequest.RoleUpdate roleUpdateRequest);

    public Long updateMemberTrack(AdminMemberRequest.TrackUpdate trackUpdateRequest);

    PagingResponse<AdminMemberResponse> getMemberByName(String name, int page, int size);
}
