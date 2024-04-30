package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.application.ApplicationCheckRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    @GetMapping
    public ResponseEntity<ApplicationResponse> getApplication(@RequestBody ApplicationCheckRequest applicationCheckRequest) {
        return ResponseEntity.ok().body(applicationService.getApplication(applicationCheckRequest));
    }

    @PostMapping
    public ResponseEntity<Long> postApplication(@RequestBody ApplicationRequest applicationRequest) {
        return ResponseEntity.ok().body(applicationService.saveApplication(applicationRequest));
    }

    @PutMapping
    public ResponseEntity<Long> updateApplication(@RequestBody ApplicationRequest applicationRequest) {
        return ResponseEntity.ok().body(applicationService.updateApplication(applicationRequest));
    }
}
