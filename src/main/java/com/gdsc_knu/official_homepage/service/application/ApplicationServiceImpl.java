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
        Member member = validateMember(email);
        if (!member.getStudentNumber().equals(studentNumber)) {
            throw new CustomException(ErrorCode.FORBIDDEN,"본인의 지원서만 접근 가능합니다.");
        }
        Application application = validateApplicationAccess(name, studentNumber);
        return new ApplicationResponse(application);
    }

    @Override
    @Transactional
    public Long saveApplication(String email, ApplicationRequest applicationRequest) {
        validateApplicationStatus(applicationRequest.getApplicationStatus());
        // 새로운 로직
        Member member = validateMember(email);
        applicationRepository.findByNameAndStudentNumber(member.getName(), member.getStudentNumber())
                .ifPresent(application -> {
                    throw new CustomException(ErrorCode.CONFLICT, "이미 작성한 지원서가 존재합니다.");
                });
        return applicationRepository.save(new Application(member, applicationRequest)).getId();
    }

    @Override
    @Transactional
    public Long updateApplication(String email, ApplicationRequest applicationRequest) {
        validateApplicationStatus(applicationRequest.getApplicationStatus());
        Member member = validateMember(email);
        Application application = validateApplicationAccess(member.getName(), member.getStudentNumber());
        application.updateApplication(member, applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }

    private void validateApplicationStatus(ApplicationStatus applicationStatus) {
        if (applicationStatus.equals(ApplicationStatus.REJECTED) || applicationStatus.equals(ApplicationStatus.APPROVED)) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "올바르지 않은 지원서 요청입니다.");
        }
    }

    private Member validateMember (String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private Application validateApplicationAccess (String name, String studentNumber) {
        Application application = applicationRepository.findByNameAndStudentNumber(name, studentNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return application;
    }
}
