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
    @Operation(summary="지원서 조회 API")
    public ResponseEntity<ApplicationResponse> getApplication(@TokenMember JwtMemberDetail jwtMemberDetail, @RequestParam String name, @RequestParam String studentNumber) {
        return ResponseEntity.ok()
                .body(applicationService.getApplication(jwtMemberDetail.getEmail(), name, studentNumber));
    }

    @PostMapping
    @Operation(summary="지원서 등록 API")
    public ResponseEntity<Long> postApplication(@TokenMember JwtMemberDetail jwtMemberDetail, @RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.saveApplication(jwtMemberDetail.getEmail(), applicationRequest));
    }

    @PutMapping
    @Operation(summary="지원서 수정 API")
    public ResponseEntity<Long> updateApplication(@TokenMember JwtMemberDetail jwtMemberDetail, @RequestBody @Valid ApplicationRequest applicationRequest) {
        return ResponseEntity.ok()
                .body(applicationService.updateApplication(jwtMemberDetail.getEmail(), applicationRequest));
    }
}
