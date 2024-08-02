package com.gdsc_knu.official_homepage.controller.admin;

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
    @Operation(summary="지원서류 통계데이터 조회 API")
    public ResponseEntity<AdminApplicationRes.Statistics> getStatistic() {
        return ResponseEntity.ok().body(applicationService.getStatistic());
    }


    @GetMapping()
    @Operation(summary="조건별 지원 서류 조회 API", description = "직렬별, 마크여부에 따라 지원서류를 조회합니다.\n" +
            "마크된것만 조회하려면 isMarked=true로 설정하세요.\n" +
            "isMarked를 비워두거나 false로 설정하면 전체가 조회됩니다.(track도 마찬가지로 비워두면 전체)")
    public ResponseEntity<PagingResponse<AdminApplicationRes.Overview>> getApplicationListByOption(
            @RequestParam(value = "track", required = false) Track track,
            @RequestParam(value = "isMarked", required = false, defaultValue = "false") boolean isMarked,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)

    {
        System.out.println("track: "+ track);
        return ResponseEntity.ok().body(applicationService.getAllApplicationsByOption(page, size, track, isMarked));
    }



}
