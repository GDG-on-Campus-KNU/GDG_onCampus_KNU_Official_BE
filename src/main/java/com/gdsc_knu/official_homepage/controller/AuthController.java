package com.gdsc_knu.official_homepage.controller;

import com.gdsc_knu.official_homepage.authentication.jwt.JwtProvider;
import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.dto.oauth.AuthorizationCode;
import com.gdsc_knu.official_homepage.dto.oauth.LoginResponseDto;
import com.gdsc_knu.official_homepage.oauth.OAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Tag(name = "Auth", description = "회원 인증(가입/로그인)관련 API")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final OAuthService oAuthService;
    private final JwtProvider jwtProvider;

    /**
     * 로컬 환경에서 google oauth 테스트를 위함.
     */
    @Hidden
    @GetMapping("/oauth/google/redirect/")
    public ResponseEntity<LoginResponseDto> googleOAuth(@RequestParam(name="code")String code){
        return ResponseEntity.ok().body(oAuthService.getGoogleAccessToken(code));
    }

    @PostMapping("api/auth/oauth")
    @Operation(summary="OAuth 회원가입/로그인 API")
    public ResponseEntity<LoginResponseDto> googleOAuth(@RequestBody AuthorizationCode code){
        return ResponseEntity.ok().body(oAuthService.getGoogleAccessToken(code.getCode()));
    }

    @GetMapping("/api/jwt/reissue")
    @Operation(summary = "토큰 재발급 API", description = "리프레시 토큰을 이용하여 엑세스 토큰과 리프레시 토큰을 재발급합니다.")
    public ResponseEntity<TokenResponse> reissueTokens(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(jwtProvider.reissueTokens(token));
    }
}
