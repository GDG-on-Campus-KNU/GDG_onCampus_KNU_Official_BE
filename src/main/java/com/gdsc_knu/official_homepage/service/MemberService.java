package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.LoginRequest;
import com.gdsc_knu.official_homepage.dto.SignupRequest;

import org.springframework.http.HttpHeaders;

public interface MemberService {
    Long signup(SignupRequest signupRequest);

    HttpHeaders login(LoginRequest loginRequest);

    HttpHeaders logout(String sessionId);
}
