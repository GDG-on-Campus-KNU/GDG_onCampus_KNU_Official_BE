package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberDeleteRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberInfoResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberRoleRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberTrackRequest;

import java.util.List;

public interface AdminMemberStatusService {
    public PagingResponse<MemberInfoResponse> getAllMemberInfos(int page, int size);

    public void deleteMember(MemberDeleteRequest memberDeleteRequest);

    public Long updateMemberRole(MemberRoleRequest memberRoleRequest);

    public Long updateMemberTrack(MemberTrackRequest memberTrackRequest);

    PagingResponse<MemberInfoResponse> getMemberByName(String name, int page, int size);
}
