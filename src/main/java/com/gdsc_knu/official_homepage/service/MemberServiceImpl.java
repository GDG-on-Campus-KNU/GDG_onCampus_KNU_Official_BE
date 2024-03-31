package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.SignupRequest;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long signup(SignupRequest signupRequest) {
        return memberRepository.save(signupRequest.toEntity()).getId();
    }
}
