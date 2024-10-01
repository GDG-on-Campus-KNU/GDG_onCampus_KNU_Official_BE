package com.gdsc_knu.official_homepage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/check")
    public String healthCheck(){
        return "status up";
    }
}
