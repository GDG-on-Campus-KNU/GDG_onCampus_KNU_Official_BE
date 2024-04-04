package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.MemberInfoRequest;
import com.gdsc_knu.official_homepage.dto.MemberInfoResponse;
import com.gdsc_knu.official_homepage.service.MemberInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="MyPage",description="마이페이지 관련 API")
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class MypageController {
    private final MemberInfoService memberInfoService;

    @GetMapping()
    @Operation(summary="사용자 정보 조회 API")
    public ResponseEntity<MemberInfoResponse> retrieveMemberInfo(@RequestParam(name = "username")Long id){
        return ResponseEntity.ok().body(memberInfoService.retrieveMemberInfo(id));
    }

    @PostMapping()
    @Operation(summary="사용자 정보 수정 API")
    public ResponseEntity<Long> updateMemberInfo(@RequestBody MemberInfoRequest memberInfoRequest,
                                                 @RequestParam(name = "username")Long id){
        return ResponseEntity.ok().body(memberInfoService.updateMemberInfo(id,memberInfoRequest));
    }
}
