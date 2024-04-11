package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.dto.oauth.LoginResponseDto;
import com.gdsc_knu.official_homepage.oauth.OAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Tag(name = "Auth", description = "회원 인증(가입/로그인)관련 API")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OAuthService oAuthService;

    @GetMapping("google/oauth")
    public ResponseEntity<LoginResponseDto> googleOAuth(@RequestParam(name="code") String code){
        //@RequestBody GoogleCode code
        return ResponseEntity.ok().body(oAuthService.getGoogleAccessToken(code));
    }

}
