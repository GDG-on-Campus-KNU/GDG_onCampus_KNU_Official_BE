package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationModel;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.application.ClassYearRepository;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final ClassYearRepository classYearRepository;

    /**
     * 본인의 지원서 조회, 다른 사람 지원서 조회 시 예외 발생
     * @param email 이메일
     * @param name 이름
     * @param studentNumber 학번
     * @return ApplicationResponse
     * @throws CustomException ErrorCode.APPLICATION_FORBIDDEN
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String email, String name, String studentNumber, Long classYearId) {
        Member member = validateMember(email);
        if (!member.getStudentNumber().equals(studentNumber) || !member.getName().equals(name)) {
            throw new CustomException(ErrorCode.APPLICATION_FORBIDDEN);
        }
        Application application = validateApplicationAccess(name, studentNumber, classYearId);
        return new ApplicationResponse(application);
    }

    /**
     * 지원서 작성, 이미 작성한 지원서가 존재한다면 예외 발생
     * @param email 이메일
     * @param applicationRequest (테크스택, 링크, 지원서 상태, 트랙, 답변)
     * @return Long 지원서 id
     * @throws CustomException ErrorCode.APPLICATION_DUPLICATED
     */
    @Override
    @Transactional
    public Long saveApplication(String email, ApplicationRequest applicationRequest) {
        validateApplicationStatus(applicationRequest.getApplicationStatus());
        validateApplicationDeadline(applicationRequest.getClassYearId());
        Member member = validateMember(email);
        applicationRepository.findByNameAndStudentNumberAndClassYearId(member.getName(), member.getStudentNumber(), applicationRequest.getClassYearId())
            .ifPresent(application -> {
                throw new CustomException(ErrorCode.APPLICATION_DUPLICATED, "이미 작성 중이거나 제출한 지원서가 있습니다.");
            });
        member.updateTrack(applicationRequest.getTrack());
        Application application =  new Application(member, createApplicationRequestDTO(applicationRequest));
        application.updateClassYear(classYearRepository.findById(applicationRequest.getClassYearId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND)));
        return applicationRepository.save(application).getId();
    }

    /**
     * 지원서 수정, 이미 최종 제출 된 지원서는 수정 불가
     * @param email 이메일
     * @param applicationRequest (테크스택, 링크, 지원서 상태, 트랙, 답변)
     * @return Long 지원서 id
     */
    @Override
    @Transactional
    public Long updateApplication(String email, ApplicationRequest applicationRequest) {
        validateApplicationStatus(applicationRequest.getApplicationStatus());
        validateApplicationDeadline(applicationRequest.getClassYearId());
        Member member = validateMember(email);
        Application application = validateApplicationAccess(member.getName(), member.getStudentNumber(), applicationRequest.getClassYearId());
        application.updateApplication(member, createApplicationRequestDTO(applicationRequest));
        application.updateClassYear(classYearRepository.findById(applicationRequest.getClassYearId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND)));
        applicationRepository.save(application);
        return application.getId();
    }

    /**
     * 입력된 지원서 상태의 유효성을 판단
     * @param applicationStatus 지원서 상태
     * @throws CustomException ErrorCode.INVALID_APPLICATION_STATE
     */
    private void validateApplicationStatus(ApplicationStatus applicationStatus) {
        try {
            ApplicationStatus.valueOf(applicationStatus.toString());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_APPLICATION_STATE);
        }
    }

    /**
     * 이메일로 회원 존재 여부 조회 (존재하지 않는 멤버라면 예외 발생)
     * @param email 이메일
     * @return Member
     * @throws CustomException ErrorCode.USER_NOT_FOUND
     */
    private Member validateMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 지원서 접근 가능 여부 판단 (지원서가 존재하지 않거나 이미 최종 제출 된 지원서라면 예외 발생)
     * @param name 이름
     * @param studentNumber 학번
     * @return Application
     * @throws CustomException ErrorCode.APPLICATION_NOT_FOUND(지원서가 존재하지 않음), ErrorCode.APPLICATION_DUPLICATED(지원서가 최종제출 됨)
     */
    private Application validateApplicationAccess(String name, String studentNumber, Long classYearId) {
        Application application = applicationRepository.findByNameAndStudentNumberAndClassYearId(name, studentNumber, classYearId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.APPLICATION_DUPLICATED);
        }
        return application;
    }

    private void validateApplicationDeadline(Long classYearId) {
        LocalDateTime now = LocalDateTime.now();
        ClassYear classYear = classYearRepository.findById(classYearId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND));
        if (now.isBefore(classYear.getApplicationStartDateTime()) || now.isAfter(classYear.getApplicationEndDateTime())) {
            throw new CustomException(ErrorCode.APPLICATION_DEADLINE_EXPIRED);
        }
    }

    private ApplicationModel createApplicationRequestDTO(ApplicationRequest applicationRequest) {
        return ApplicationModel.builder()
                .techStack(applicationRequest.getTechStack())
                .links(applicationRequest.getLinks())
                .applicationStatus(applicationRequest.getApplicationStatus())
                .track(applicationRequest.getTrack())
                .answers(applicationRequest.getAnswers())
                .build();
    }
}
