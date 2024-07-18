package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRes;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Application", description = "서류확인 관련 API")
@RestController
@RequestMapping("/api/admin/application")
@RequiredArgsConstructor
public class AdminApplicationController {
    private final AdminApplicationService applicationService;
    @GetMapping("/statistic")
    @Operation(summary="지원서 통계데이터 조회 API")
    public ResponseEntity<AdminApplicationRes.Statistics> getStatistic() {
        return ResponseEntity.ok().body(applicationService.getStatistic());
    }



}
