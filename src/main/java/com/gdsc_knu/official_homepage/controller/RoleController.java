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

@Tag(name = "Permission", description = "사용자 상태(ROLE) 관련 API")
@RestController
@RequestMapping("api/user/permission")
@RequiredArgsConstructor
public class RoleController {
    private final PermissionSerivce permissionSerivce;
    @PatchMapping("approve")
    @Operation(summary="MEMBER 승인 API")
    public ResponseEntity<String> approveMember(@TokenMember JwtMemberDetail jwtMemberDetail,
                                        @RequestBody UserRoleRequest userRoleRequest){
        Long count = permissionSerivce.approveMember(userRoleRequest);
        return ResponseEntity.ok().body(count+"명의 멤버가 승인되었습니다.");
    }

}
