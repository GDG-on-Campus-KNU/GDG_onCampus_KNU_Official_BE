package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRequest;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Tag(name = "Admin Application", description = "서류확인 관련 API")
@RestController
@RequestMapping("/api/admin/application")
@RequiredArgsConstructor
public class AdminApplicationController {
    private final AdminApplicationService applicationService;
    private static final LocalDate ACTIVE_DATE = LocalDate.of(2025,1,19);

    @GetMapping("statistic")
    @Operation(summary="지원서류 통계데이터 조회 API", description = "classYearId를 비워두거나 false로 설정하면 전체가 조회됩니다.")
    public ResponseEntity<AdminApplicationResponse.Statistics> getStatistic(
            @RequestParam(value = "classYearId", required = false) Long classYearId)
    {
        return ResponseEntity.ok().body(applicationService.getStatistic(classYearId));
    }

    @GetMapping("statistic/track")
    @Operation(summary="직렬별, 전체 지원서류 개수 조회 API", description = "classYearId를 비워두거나 false로 설정하면 전체가 조회됩니다.")
    public ResponseEntity<Map<String, Integer>> getTrackStatistic(
            @RequestParam(value = "classYearId", required = false) Long classYearId)
    {
        return ResponseEntity.ok().body(applicationService.getTrackStatistic(classYearId));
    }


    @GetMapping
    @Operation(summary="조건별 지원 서류 조회 API", description = """
            직렬별, 서류합격 여부에 따라 지원서류를 조회합니다.

            마크된것만 조회하려면 isMarked=true로 설정하세요.
            
            isMarked를 비워두거나 false로 설정하면 전체가 조회됩니다.(track, classYearId도 마찬가지로 비워두면 전체)""")
    public ResponseEntity<PagingResponse<AdminApplicationResponse.Overview>> getApplicationListByOption(
            @RequestParam(value = "track", required = false) Track track,
            @RequestParam(value = "isMarked", required = false, defaultValue = "false") Boolean isMarked,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "classYearId", required = false) Long classYearId)

    {
        return ResponseEntity.ok().body(applicationService.getApplicationsByOption(page, size, track, isMarked, classYearId));
    }


    @GetMapping("search")
    @Operation(summary="지원서류 이름 검색 API", description = "이름으로 지원서류를 조회합니다.(임시저장된 것도 함께 조회됩니다)")
    public ResponseEntity<PagingResponse<AdminApplicationResponse.Overview>> getApplicationByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        return ResponseEntity.ok().body(applicationService.getApplicationsByName(page, size, name));
    }


    @PatchMapping("mark")
    @Operation(summary="지원서류 마킹 API", description = """
            지원서류의 서류합격/불합격을 결정합니다.
            
            마킹된 상태 -> 마킹되지 않은 상태, 마킹되지 않은 상태 -> 마킹된 상태로 변경됩니다.
            """)
    public ResponseEntity<Void> markApplication(@RequestParam("id") Long id) {
        applicationService.markApplication(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("status")
    @Operation(summary="지원서류 합격/불합격 API", description = """
            지원서류의 최종합격/불합격을 결정합니다.
            
            합불에 따라 메일이 발송됩니다.
            
            2024.09.14 이후로 처리가 가능합니다.
            """)
    public ResponseEntity<AdminApplicationResponse.Result> decideApplication(@RequestParam("id") Long id,
                                                          @RequestParam("status") ApplicationStatus status){
        if (LocalDate.now().isBefore(ACTIVE_DATE)) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "합격, 불합격 처리 기간이 아닙니다.");
        }
        applicationService.decideApplication(id, status);
        return ResponseEntity.ok().body(AdminApplicationResponse.Result.from(HttpStatus.OK, "지원서류의 " + status + " 처리가 완료되었습니다."));
    }


    @GetMapping("detail")
    @Operation(summary="지원서류 상세 조회 API", description = "지원서류의 상세정보를 조회합니다.")
    public ResponseEntity<AdminApplicationResponse.Detail> getApplicationDetail(@RequestParam("id") Long id) {
        return ResponseEntity.ok().body(applicationService.getApplicationDetail(id));
    }


    @PatchMapping("note")
    @Operation(summary="지원서류 메모 API", description = "지원서류에 메모를 합니다.")
    public ResponseEntity<Void> noteApplication(@RequestParam("id") Long id,
                                                @RequestBody AdminApplicationRequest.Append request) {
        applicationService.noteApplication(id, request.getNote(), request.getVersion());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
