package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoAdd;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoUpdate;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoResponse;
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
                               @RequestBody MemberInfoAdd request){
        System.out.println("멤버정보");
        System.out.println(jwtMemberDetail.getEmail());
        System.out.println(jwtMemberDetail.getMember().getEmail());
        System.out.println(jwtMemberDetail.getMember().getId());
        System.out.println("dto정보");
        System.out.println(request.getName());
        System.out.println(request.getAge());
        System.out.println(request.getMajor());
        System.out.println(request.getStudentNumber());
        System.out.println(request.getPhoneNumber());
        memberInfoService.addMemberInfo(jwtMemberDetail.getMember().getId(),request);
    }

    @GetMapping()
    @Operation(summary="사용자 정보 조회 API")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail){
        return ResponseEntity.ok().body(memberInfoService.getMemberInfo(jwtMemberDetail.getMember().getId()));
    }

    @PutMapping()
    @Operation(summary="사용자 정보 수정 API")
    public void updateMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail,
                                 @RequestBody MemberInfoUpdate memberInfoUpdate){
        System.out.println("멤버정보");
        System.out.println(jwtMemberDetail.getEmail());
        System.out.println(jwtMemberDetail.getMember().getEmail());
        System.out.println(jwtMemberDetail.getMember().getId());
        System.out.println("dto정보");
        System.out.println(memberInfoUpdate.getName());
        System.out.println(memberInfoUpdate.getProfileUrl());
        System.out.println(memberInfoUpdate.getAge());
        System.out.println(memberInfoUpdate.getMajor());
        System.out.println(memberInfoUpdate.getStudentNumber());
        System.out.println(memberInfoUpdate.getEmail());
        System.out.println(memberInfoUpdate.getPhoneNumber());
        System.out.println(memberInfoUpdate.getIntroduction());

        memberInfoService.updateMemberInfo(jwtMemberDetail.getMember().getId(), memberInfoUpdate);
    }
}
