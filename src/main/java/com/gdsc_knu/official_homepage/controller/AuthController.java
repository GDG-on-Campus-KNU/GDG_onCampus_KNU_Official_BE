package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.LoginRequest;
import com.gdsc_knu.official_homepage.dto.SignupRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;

@Tag(name = "Auth", description = "회원 인증(가입/로그인)관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
//    private final MemberService memberService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<Long> signup(@RequestBody @Valid SignupRequest signupRequest) {
//        Long memberId = memberService.signup(signupRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
//        HttpHeaders headers = memberService.login(loginRequest);
//        return ResponseEntity.ok().headers(headers).build();
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(@CookieValue(name = "JSESSIONID") String sessionId) {
//        HttpHeaders headers = memberService.logout(sessionId);
//        return ResponseEntity.ok().headers(headers).build();
//    }
}
