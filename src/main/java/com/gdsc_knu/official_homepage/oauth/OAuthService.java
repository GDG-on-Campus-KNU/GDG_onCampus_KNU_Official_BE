package com.gdsc_knu.official_homepage.oauth;

import com.gdsc_knu.official_homepage.authentication.jwt.JwtProvider;
import com.gdsc_knu.official_homepage.dto.jwt.TokenResponse;
import com.gdsc_knu.official_homepage.dto.oauth.GoogleToken;
import com.gdsc_knu.official_homepage.dto.oauth.GoogleUserInfo;
import com.gdsc_knu.official_homepage.dto.oauth.LoginResponseDto;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    /**
     * 1. code로 액세스 토큰 받아오기
     * 2. 액세스 토큰으로 사용자 정보 요청
     * 3. 저장 및 jwt 반환
     */
    @Value("${oauth2.google.client-id}")
    String clientId;
    @Value("${oauth2.google.client-secret}")
    String clientSecret;
    @Value("${oauth2.google.redirect-uri}")
    String redirectUrl;
    @Value("${oauth2.google.token-uri}")
    String tokenUrl;
    @Value("${oauth2.google.userinfo-uri}")
    String userInfoUrl;

    private Member saveNewMember(GoogleUserInfo googleUserInfo){
        return memberRepository.save(googleUserInfo.toMember());
    }

    public LoginResponseDto getGoogleAccessToken(String code) {
        String decode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        GoogleToken googleToken = getAccessToken(decode);
        GoogleUserInfo googleUserInfo = getUserInfo(googleToken.getAccess_token());

        Member member = memberRepository.findByEmail(googleUserInfo.getEmail())
                .orElseGet(() -> saveNewMember(googleUserInfo));

        TokenResponse response = jwtProvider.issueTokens(member.getId(), member.getEmail(), member.getRole());
        return new LoginResponseDto(
                member.getId(),
                member.getRole()==Role.ROLE_TEMP,
                response.getAccessToken(),
                response.getRefreshToken());
    }

    private GoogleToken getAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri",redirectUrl);
        params.add("grant_type", "authorization_code");


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params,headers);
        ResponseEntity<GoogleToken> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                GoogleToken.class
        );

        return response.getBody();
    }

    private GoogleUserInfo getUserInfo(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.POST,
                request,
                GoogleUserInfo.class
        );

        return response.getBody();
    }

}
