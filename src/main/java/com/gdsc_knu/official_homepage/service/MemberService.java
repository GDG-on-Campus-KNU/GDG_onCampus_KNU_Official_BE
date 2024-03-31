package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.SignupRequest;

public interface MemberService {
    Long signup(SignupRequest signupRequest);
}
