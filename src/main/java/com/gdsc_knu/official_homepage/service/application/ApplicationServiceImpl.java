package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;

    /**
     * 본인의 지원서 조회, 다른 사람 지원서 조회 시 예외 발생
     * @param email 이메일
     * @param name 이름
     * @param studentNumber 학번
     * @return ApplicationResponse
     * @throws CustomException ErrorCode.FORBIDDEN
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String email, String name, String studentNumber) {
        Member member = validateMember(email);
        if (!member.getStudentNumber().equals(studentNumber) || !member.getName().equals(name)) {
            throw new CustomException(ErrorCode.FORBIDDEN, "본인의 지원서만 접근 가능합니다.");
        }
        Application application = validateApplicationAccess(name, studentNumber);
        return new ApplicationResponse(application);
    }

    /**
     * 지원서 작성, 이미 작성한 지원서가 존재한다면 예외 발생
     * @param email 이메일
     * @param applicationRequest (테크스택, 링크, 지원서 상태, 트랙, 답변)
     * @return Long 지원서 id
     * @throws CustomException ErrorCode.CONFLICT
     */
    @Override
    @Transactional
    public Long saveApplication(String email, ApplicationRequest applicationRequest) {
        validateApplicationStatus(applicationRequest.getApplicationStatus());
        Member member = validateMember(email);
        applicationRepository.findByNameAndStudentNumber(member.getName(), member.getStudentNumber())
                .ifPresent(application -> {
                    throw new CustomException(ErrorCode.CONFLICT, "이미 작성한 지원서가 존재합니다.");
                });
        member.updateTrack(applicationRequest.getTrack());
        return applicationRepository.save(new Application(member, applicationRequest)).getId();
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
        Member member = validateMember(email);
        Application application = validateApplicationAccess(member.getName(), member.getStudentNumber());
        application.updateApplication(member, applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }

    /**
     * 지원서 접근 가능 상태를 판단 (이미 최종 제출 된 지원서는 접근 불가, 예외 발생)
     * @param applicationStatus 지원서 상태
     * @throws CustomException ErrorCode.INVALID_INPUT
     */
    private void validateApplicationStatus(ApplicationStatus applicationStatus) {
        if (applicationStatus.equals(ApplicationStatus.REJECTED) || applicationStatus.equals(ApplicationStatus.APPROVED)) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "올바르지 않은 지원서 요청입니다.");
        }
    }

    /**
     * 이메일로 회원 존재 여부 조회 (존재하지 않는 멤버라면 예외 발생)
     * @param email - 이메일
     * @return Member
     * @throws CustomException ErrorCode.NOT_FOUND
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
     * @throws CustomException ErrorCode.NOT_FOUND(지원서가 존재하지 않음), ErrorCode.CONFLICT(지원서가 최종제출 됨)
     */
    private Application validateApplicationAccess(String name, String studentNumber) {
        Application application = applicationRepository.findByNameAndStudentNumber(name, studentNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "회원님의 지원서가 존재하지 않습니다."));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return application;
    }
}
