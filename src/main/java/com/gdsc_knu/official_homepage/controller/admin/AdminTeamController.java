package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamChangeRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamCreateRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.service.admin.AdminTeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Team", description = "관리자 팀 관리용 API")
@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {
    private final AdminTeamService adminTeamService;

    @GetMapping()
    public ResponseEntity<List<AdminTeamResponse>> getTeamTags(@TokenMember JwtMemberDetail jwtMemberDetail) {
        return ResponseEntity.ok().body(adminTeamService.getTeamInfos());
    }

    @PostMapping()
    public ResponseEntity<Long> createTeam(
            @TokenMember JwtMemberDetail jwtMemberDetail,
            @RequestBody AdminTeamCreateRequest adminTeamCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTeamService.createTeam(adminTeamCreateRequest));
    }

    @PostMapping("/{teamId}/subTeam")
    public ResponseEntity<Long> createSubTeam(
            @TokenMember JwtMemberDetail jwtMemberDetail,
            @PathVariable("teamId") Long parentTeamId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTeamService.createSubTeam(parentTeamId));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<List<AdminMemberResponse>> getTeamMembers(
            @TokenMember JwtMemberDetail jwtMemberDetail,
            @PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok().body(adminTeamService.getTeamMembers(teamId));
    }

    @PutMapping()
    public ResponseEntity<Long> changeTeamMember(
            @TokenMember JwtMemberDetail jwtMemberDetail,
            @RequestBody AdminTeamChangeRequest adminTeamChangeRequest) {
        return ResponseEntity.ok().body(adminTeamService.changeTeamMember(adminTeamChangeRequest));
    }
}
