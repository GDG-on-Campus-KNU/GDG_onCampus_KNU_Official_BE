package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.SignupRequest;
import com.gdsc_knu.official_homepage.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원 인증(가입/로그인)관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody @Valid SignupRequest signupRequest) {
        Long memberId = memberService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }
}
