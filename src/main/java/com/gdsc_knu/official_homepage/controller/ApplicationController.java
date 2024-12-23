package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.service.application.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Application", description = "지원하기(지원서 CRU)관련 API")
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    @GetMapping
    @Operation(summary="지원서 조회 API",
            description = """
                    이름, 학번으로 지원서 조회를 조회합니다.
                    
                    로그인한 토큰의 정보와 대조해 본인의 지원서만 조회합니다.
                    
                    최종제출 상태의 지원서는 조회가 불가능합니다.
                    """)
    public ResponseEntity<ApplicationResponse> getApplication(@TokenMember JwtMemberDetail jwtMemberDetail,
                                                              @RequestParam String name,
                                                              @RequestParam String studentNumber,
                                                              @RequestParam Long classYearId) {
        return ResponseEntity.ok()
                .body(applicationService.getApplication(jwtMemberDetail.getEmail(), name, studentNumber, classYearId));
    }

    @PostMapping
    @Operation(summary="지원서 등록 API",
            description = """
                    지원서를 초기 저장합니다.
                    
                    개인정보는 로그인한 토큰에서 가져와 자동 저장 됩니다.
                    
                    이미 저장한 지원서가 있다면 중복 저장이 되지 않습니다.
                    """)
    public ResponseEntity<Long> postApplication(@TokenMember JwtMemberDetail jwtMemberDetail, @RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.saveApplication(jwtMemberDetail.getEmail(), applicationRequest));
    }

    @PatchMapping
    @Operation(summary="지원서 수정 API",
            description = """
                    이미 저장된 지원서를 수정합니다.
                    
                    최종 제출된 지원서는 수정이 불가능합니다.
                    """)
    public ResponseEntity<Long> updateApplication(@TokenMember JwtMemberDetail jwtMemberDetail, @RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.ok()
                .body(applicationService.updateApplication(jwtMemberDetail.getEmail(), applicationRequest));
    }
}
