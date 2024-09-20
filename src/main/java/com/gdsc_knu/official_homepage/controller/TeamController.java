package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Team", description = "팀 관련 API")
@RestController
@RequestMapping("api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/{teamId}/member")
    @Operation(summary = "팀원 정보 조회 API",
            description = "해당 팀의 팀원목록(유저 ID, 이름, 직렬)을 반환합니다. (미배치된 팀은 조회되지 않습니다.)")
    public ResponseEntity<List<TeamResponse.MemberInfo>> getTeamMembers(@PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok().body(teamService.getTeamMember(teamId));
    }
}