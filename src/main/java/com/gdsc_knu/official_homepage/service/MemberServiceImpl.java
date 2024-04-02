package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.LoginRequest;
import com.gdsc_knu.official_homepage.dto.SignupRequest;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    @Override
    @Transactional
    public Long signup(SignupRequest signupRequest) {
        return memberRepository.save(signupRequest.toEntity()).getId();
    }

    @Override
    @Transactional
    public HttpHeaders logout(String sessionId) {
        Member member = memberRepository.findBySessionId(sessionId);
        if (member != null) {
            httpSession.invalidate();

            member.updateSessionId(null);

            HttpHeaders headers = new HttpHeaders();
            Cookie cookie = new Cookie("sessionId", null);
            cookie.setMaxAge(0); // 만료일을 0으로 설정하여 삭제
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return headers;
        }
        else {
            throw new IllegalArgumentException("로그인 상태가 아닙니다.");
        }
    }

    @Override
    @Transactional
    public HttpHeaders login(LoginRequest loginRequest) {
        if (!isValidUser(loginRequest.getEmail(), loginRequest.getStudentNumber())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        // 쿠키에 세션 ID를 담아서 응답
        HttpHeaders headers = new HttpHeaders();
        Cookie cookie = new Cookie("JSESSIONID", httpSession.getId());
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        Member member = memberRepository.getByEmail(loginRequest.getEmail());
        member.updateSessionId(httpSession.getId());
        memberRepository.save(member);

        return headers;
    }

    private boolean isValidUser(String email, String studentNumber) {
        Member member = memberRepository.findByEmailAndStudentNumber(email, studentNumber);
        return member != null;
    }
}
