package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Mail", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MailTestController {
    private final MailService mailService;

    @GetMapping("/mail")
    public void mailTest() {
        mailService.send();
    }
}
