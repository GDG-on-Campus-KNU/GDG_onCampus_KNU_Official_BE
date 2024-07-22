package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRes;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Application", description = "서류확인 관련 API")
@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
public class AdminApplicationController {
    private final AdminApplicationService applicationService;
    @GetMapping("/statistic")
    @Operation(summary="지원서 통계데이터 조회 API")
    public ResponseEntity<AdminApplicationRes.Statistics> getStatistic() {
        return ResponseEntity.ok().body(applicationService.getStatistic());
    }

    @GetMapping("{isMarked}")
    public ResponseEntity<PagingResponse<AdminApplicationRes.Overview>> getApplicationList(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable("isMarked") boolean isMarked)
    {
        return ResponseEntity.ok().body(applicationService.getAllApplications(page, size, isMarked));
    }

    @GetMapping("{track}/{isMarked}")
    public ResponseEntity<PagingResponse<AdminApplicationRes.Overview>> getApplicationListByOption(
            @TokenMember JwtMemberDetail memberDetail,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable("track") Track track,
            @PathVariable("isMarked") boolean isMarked)

    {
        return ResponseEntity.ok().body(applicationService.getAllApplicationsByOption(page, size, track, isMarked));
    }



}