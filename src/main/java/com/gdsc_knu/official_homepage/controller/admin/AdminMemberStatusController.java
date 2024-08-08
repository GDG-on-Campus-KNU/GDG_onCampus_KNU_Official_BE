package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberDeleteRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberInfoResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberRoleRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberTrackRequest;
import com.gdsc_knu.official_homepage.service.admin.AdminMemberStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Member Status", description = "멤버 상태 관련 API")
@RequestMapping("/api/admin/member")
public class AdminMemberStatusController {
    private final AdminMemberStatusService adminMemberStatusService;

    @GetMapping()
    @Operation(summary = "모든 멤버 정보 조회 API")
    public ResponseEntity<PagingResponse<MemberInfoResponse>> getAllMemberInfos(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(adminMemberStatusService.getAllMemberInfos(page, size));
    }

    @GetMapping()
    @Operation(summary = "이름검색으로 멤버 정보 조회 API")
    public ResponseEntity<PagingResponse<MemberInfoResponse>> getMemberByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(adminMemberStatusService.getMemberByName(name, page, size));
    }

    @DeleteMapping()
    @Operation(summary = "회원 삭제 API")
    public ResponseEntity<Void> deleteMember(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestBody MemberDeleteRequest memberDeleteRequest) {
        adminMemberStatusService.deleteMember(memberDeleteRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/role")
    @Operation(summary = "회원 권한 수정 API")
    public ResponseEntity<Long> updateMemberRole(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestBody MemberRoleRequest memberRoleRequest) {
        return ResponseEntity.ok(adminMemberStatusService.updateMemberRole(memberRoleRequest));
    }

    @PatchMapping("/track")
    @Operation(summary = "회원 직렬 수정 API")
    public ResponseEntity<Long> updateMemberTrack(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestBody MemberTrackRequest memberTrackRequest) {
        return ResponseEntity.ok(adminMemberStatusService.updateMemberTrack(memberTrackRequest));
    }
}
