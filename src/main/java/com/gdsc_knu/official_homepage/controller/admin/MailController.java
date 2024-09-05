package com.gdsc_knu.official_homepage.controller.admin;

import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.gdsc_knu.official_homepage.service.MailService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


@Tag(name = "Mail", description = "메일 전송 API")
@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @Hidden
    @PostMapping("/failed-mail")
    @Operation(summary = "(전송실패한) 최종 합/불 지원자마다 메일 일괄 발송 API", description = "전송 실패한 메일을 일괄적으로 다시 발송 후, 재전송한 메일목록 반환")
    public ResponseEntity<String> mailRetry() {
        Set<Object> emails = mailService.sendAllFailed();
        return ResponseEntity.ok().body("재전송 모두 성공");
    }

    @GetMapping("/failed-mail")
    @Operation(summary = "전송 실패한 메일 목록 조회 API", description = "전송 실패한 메일 목록을 조회합니다.")
    public ResponseEntity<List<String>> getFailedMailList() {
        return ResponseEntity.ok().body(mailService.getFailedMailList());
    }
}
