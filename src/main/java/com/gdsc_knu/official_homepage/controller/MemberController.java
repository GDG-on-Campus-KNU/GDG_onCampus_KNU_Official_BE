package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.service.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberInfoService memberInfoService;
    @PostMapping("additional-info")
    @Operation(summary="신규가입 추가정보 입력 API")
    public void additionalInfo(@TokenMember JwtMemberDetail jwtMemberDetail,
                               @RequestBody MemberRequest.Append request){
        memberInfoService.addMemberInfo(jwtMemberDetail.getId(),request);
    }

    @GetMapping()
    @Operation(summary="사용자 정보 조회 API")
    public ResponseEntity<MemberResponse> getMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail){
        return ResponseEntity.ok().body(memberInfoService.getMemberInfo(jwtMemberDetail.getId()));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary="사용자 정보 수정 API")
    public void updateMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail,
                                 @ModelAttribute MemberRequest.Update request){
        memberInfoService.updateMemberInfo(jwtMemberDetail.getId(), request);
    }

    @GetMapping("/teams")
    @Operation(summary="사용자가 속한 팀 정보 조회 API")
    public ResponseEntity<List<TeamInfoResponse>> getMemberTeamInfo(@TokenMember JwtMemberDetail jwtMemberDetail){
        return ResponseEntity.ok().body(memberInfoService.getMemberTeamInfo(jwtMemberDetail.getId()));
    }
}
