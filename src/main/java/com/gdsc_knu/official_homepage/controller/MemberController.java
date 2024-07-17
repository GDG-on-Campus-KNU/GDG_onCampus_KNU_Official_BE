package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.service.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        memberInfoService.addMemberInfo(jwtMemberDetail.getMember().getId(),request);
    }

    @GetMapping()
    @Operation(summary="사용자 정보 조회 API")
    public ResponseEntity<MemberResponse> getMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail){
        return ResponseEntity.ok().body(memberInfoService.getMemberInfo(jwtMemberDetail.getMember().getId()));
    }

    @PutMapping()
    @Operation(summary="사용자 정보 수정 API")
    public void updateMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail,
                                 @RequestBody MemberRequest.Update request){
        memberInfoService.updateMemberInfo(jwtMemberDetail.getMember().getId(), request);
    }
}
