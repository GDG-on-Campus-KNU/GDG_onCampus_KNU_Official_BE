package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamChangeRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamCreateRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.service.admin.AdminTeamService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "모든 팀 정보 조회 API", description = "존재하는 모든 팀의 정보를 조회 합니다.")
    public ResponseEntity<List<AdminTeamResponse>> getTeamTags() {
        return ResponseEntity.ok().body(adminTeamService.getTeamInfos());
    }

    @PostMapping()
    @Operation(summary = "부모 팀 생성 API",
            description =
                    """
                    부모 팀 의미: 'Study-BE', 'Study-FE', '1차 프로젝트' 등의 주제별로 나눠진 대분류 팀.
                    
                    팀 생성 시, 팀 이름(필수)과 트랙(선택)을 입력 받아 팀을 생성합니다.
                    
                    트랙 입력이 없으면 전체 멤버를 직렬과 상관 없이 해당 팀에 소속 시킵니다.
                    """)
    public ResponseEntity<Long> createTeam(@RequestBody AdminTeamCreateRequest adminTeamCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTeamService.createTeam(adminTeamCreateRequest));
    }

    @PostMapping("/{teamId}/subTeam")
    @Operation(summary = "서브 팀 생성 API",
            description =
                    """
                    서브 팀 의미: 'Study-BE 1팀', 'Study-BE 2팀', ... 'Study-BE x팀' 형태로 뒤에 숫자로 나눠진 소분류 팀.
                    
                    부모 팀의 ID를 입력 받아 해당 팀의 하위 팀을 생성 합니다.
                    """)

    public ResponseEntity<Long> createSubTeam(@PathVariable("teamId") Long parentTeamId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminTeamService.createSubTeam(parentTeamId));
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "팀원 정보 조회 API",
            description =
                    """
                    팀 ID를 입력 받아 해당 팀의 팀원 정보를 조회합니다.
                    
                    이름, 학번, 프로필 이미지 url을 반환합니다.
                    
                    미배치 팀원은 부모 팀에 속해있으며, 배치된 팀원은 서브 팀에 속해있습니다.
                    """)
    public ResponseEntity<List<AdminMemberResponse>> getTeamMembers(@PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok().body(adminTeamService.getTeamMembers(teamId));
    }

    @PutMapping()
    @Operation(summary = "팀원 변경 API",
            description =
                    """
                    기존 팀 ID, 옮길 팀 ID, 멤버 ID를 받아 멤버의 소속 팀을 변경합니다.
                    
                    존재하지 않거나 동일한 팀으로 이동할 수 없습니다.
                    """)
    public ResponseEntity<Long> changeTeamMember(@RequestBody AdminTeamChangeRequest adminTeamChangeRequest) {
        return ResponseEntity.ok().body(adminTeamService.changeTeamMember(adminTeamChangeRequest));
    }
}
