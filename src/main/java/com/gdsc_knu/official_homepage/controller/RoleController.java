package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.role.UserRoleRequest;
import com.gdsc_knu.official_homepage.service.PermissionSerivce;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Permission")
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class RoleController {
    private final PermissionSerivce permissionSerivce;

    @PatchMapping("core")
    @Operation(summary="❗core 권한으로 테스트하기 위한 임시 API ❗", description = "매번 sql update 하기 번거로워서 임시로 만듦")
    public ResponseEntity<String> coreMember(@TokenMember JwtMemberDetail jwtMemberDetail){
        permissionSerivce.toCore(jwtMemberDetail.getId());
        return ResponseEntity.ok().body(jwtMemberDetail.getUsername() + " core로 변경되었습니다.");
    }

}
