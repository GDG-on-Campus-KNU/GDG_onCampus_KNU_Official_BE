package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.service.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberInfoService memberInfoService;
    @PutMapping()
    @Operation(summary="신규가입 추가정보 입력 API")
    public void additionalInfo(@TokenMember JwtMemberDetail jwtMemberDetail,
                               @RequestBody MemberRequest.Append request){
        memberInfoService.addMemberInfo(jwtMemberDetail.getId(),request);
    }

    @GetMapping()
    @Operation(summary="사용자 정보 조회 API")
    public ResponseEntity<MemberResponse.Main> getMemberInfo(@TokenMember JwtMemberDetail jwtMemberDetail){
        return ResponseEntity.ok().body(memberInfoService.getMemberInfo(jwtMemberDetail.getId()));
    }

    @PatchMapping()
    @Operation(summary="직렬, 권한 변경 편의 API (❗개발 API 아님❗)")
    public ResponseEntity<Member> getMember(@RequestParam(required = true) Track track,
                                            @RequestParam(required = true) Role role,
                                            @RequestParam(name = "email") String email) {
        return ResponseEntity.ok().body(memberInfoService.getMemberAdmin(email, track, role));
    }


}
