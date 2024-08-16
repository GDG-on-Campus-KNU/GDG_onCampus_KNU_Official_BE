package com.gdsc_knu.official_homepage.controller.admin;

import com.gdsc_knu.official_homepage.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Mail", description = "메일 전송 API")
@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/mail")
    @Operation(summary = "최종 합/불 지원자마다 메일 발송 API")
    public void mailTest() {
        mailService.send();
    }
}
