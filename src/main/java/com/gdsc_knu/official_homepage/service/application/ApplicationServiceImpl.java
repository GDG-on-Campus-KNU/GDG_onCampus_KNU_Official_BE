package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String email, String name, String studentNumber) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 회원정보입니다."));
        if (!member.getStudentNumber().equals(studentNumber)) {
            throw new IllegalArgumentException("본인의 지원서만 조회 가능합니다.");
        }
        Application application = applicationRepository.findByNameAndStudentNumber(name, studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("작성하신 지원서가 없습니다."));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return new ApplicationResponse(application);
    }

    @Override
    @Transactional
    public Long saveApplication(String email, ApplicationRequest applicationRequest) {
        checkValidApplicationStatus(applicationRequest.getApplicationStatus());
        // 새로운 로직
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 회원정보입니다."));
        applicationRepository.findByNameAndStudentNumber(member.getName(), member.getStudentNumber())
                .ifPresent(application -> {
                    throw new IllegalArgumentException("이미 작성한 지원서가 존재합니다.");
                });
        return applicationRepository.save(new Application(member, applicationRequest)).getId();
    }

    @Override
    @Transactional
    public Long updateApplication(String email, ApplicationRequest applicationRequest) {
        checkValidApplicationStatus(applicationRequest.getApplicationStatus());
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 회원정보입니다."));
        Application application = applicationRepository.findByNameAndStudentNumber(member.getName(), member.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 존재하지 않습니다."));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        application.updateApplication(member, applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }

    private void checkValidApplicationStatus(ApplicationStatus applicationStatus) {
        if (applicationStatus.equals(ApplicationStatus.REJECTED) || applicationStatus.equals(ApplicationStatus.APPROVED)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }
}
