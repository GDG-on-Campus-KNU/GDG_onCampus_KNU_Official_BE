package com.gdsc_knu.official_homepage.application.service;

import com.gdsc_knu.official_homepage.ClearDatabase;
import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.dto.application.ApplicationModel;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.application.ClassYearRepository;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.service.application.ApplicationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.*;
import static com.gdsc_knu.official_homepage.member.MemberTestEntityFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(ClearDatabase.class)
public class ApplicationServiceTest {
    @Autowired private ApplicationService applicationService;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ClassYearRepository classYearRepository;
    @Autowired private ClearDatabase clearDatabase;

    @AfterEach
    void tearDown() {
        clearDatabase.each("application");
        clearDatabase.each("class_year");
    }


    @Test
    @DisplayName("타인의 지원서를 열람할 경우 예외가 발생한다.")
    void FailedGetApplication() {
        // given
        Application application = createApplication(null, Track.AI, ApplicationStatus.TEMPORAL);
        ClassYear classYear = createClassYear(1L);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        application.updateClassYear(classYear);
        application.updateApplication(member, createApplicationModel());
        applicationRepository.save(application);
        Member otherMember = createMember(2L);
        memberRepository.save(otherMember);
        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                applicationService.getApplication(member.getEmail(), otherMember.getName(),
                        otherMember.getStudentNumber(), classYear.getId()));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.APPLICATION_FORBIDDEN);
    }

    @Test
    @DisplayName("지원서 정보가 정상적으로 저장(첫제출) 된다.")
    @Transactional
    void saveApplication() {
        // given
        Long classYearId = 1L;
        String techStack = "Java";
        String links = "https://github.com";
        Track track = Track.BACK_END;
        ApplicationStatus applicationStatus = ApplicationStatus.TEMPORAL;
        List<ApplicationAnswerDTO> answerList = List.of(
                ApplicationAnswerDTO.builder()
                        .questionNumber(1)
                        .answer("답변1")
                        .build(),
                ApplicationAnswerDTO.builder()
                        .questionNumber(2)
                        .answer("답변2")
                        .build()
        );
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .classYearId(classYearId)
                .techStack(techStack)
                .links(links)
                .track(track)
                .answers(answerList)
                .applicationStatus(applicationStatus)
                .build();

        ClassYear classYear = createClassYear(classYearId);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);

        String email = member.getEmail();
        String name = member.getName();
        String studentNumber = member.getStudentNumber();
        String major = member.getMajor();
        String phoneNumber = member.getPhoneNumber();

        // when
        Application application = applicationRepository.findById(
                applicationService.saveApplication(member.getEmail(), applicationRequest)).orElseThrow();
        // then
        assertThat(application.getClassYear().getId()).isEqualTo(classYearId);
        assertThat(application.getTechStack()).isEqualTo(techStack);
        assertThat(application.getLinks()).isEqualTo(links);
        assertThat(application.getTrack()).isEqualTo(track);
        assertThat(application.getApplicationStatus()).isEqualTo(applicationStatus);
        assertThat(application.getEmail()).isEqualTo(email);
        assertThat(application.getName()).isEqualTo(name);
        assertThat(application.getStudentNumber()).isEqualTo(studentNumber);
        assertThat(application.getMajor()).isEqualTo(major);
        assertThat(application.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(application.getAnswers().size()).isEqualTo(answerList.size());
        for (int i = 0; i < answerList.size(); i++) {
            assertThat(application.getAnswers().get(i).getQuestionNumber()).isEqualTo(answerList.get(i).getQuestionNumber());
            assertThat(application.getAnswers().get(i).getAnswer()).isEqualTo(answerList.get(i).getAnswer());
        }
    }

    @Test
    @DisplayName("동일한 기수에 동일한 지원자가 두 번째 지원서를 첫 제출할 경우 예외가 발생한다.")
    void FailedSaveApplication() {
        // given
        Long classYearId = 1L;
        Application application = createApplication(null, Track.AI, ApplicationStatus.TEMPORAL);
        ClassYear classYear = createClassYear(classYearId);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        application.updateClassYear(classYear);
        application.updateApplication(member, createApplicationModel());
        applicationRepository.save(application);
        ApplicationRequest applicationRequest = createApplicationRequest(classYearId);
        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                applicationService.saveApplication(member.getEmail(), applicationRequest));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.APPLICATION_CONFLICT);
    }

    @Test
    @DisplayName("데드라인을 넘어서 지원서를 제출 시 예외가 발생한다.")
    void FailedSaveApplicationDeadline() {
        // given
        Long classYearId = 1L;
        Application application = createApplication(null, Track.AI, ApplicationStatus.TEMPORAL);
        ClassYear classYear = createExpiredClassYear(1L);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        application.updateClassYear(classYear);
        application.updateApplication(member, createApplicationModel());
        applicationRepository.save(application);
        ApplicationRequest applicationRequest = createApplicationRequest(classYearId);
        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                applicationService.saveApplication(member.getEmail(), applicationRequest));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.APPLICATION_DEADLINE_EXPIRED);
    }

    @Test
    @DisplayName("지원서 정보가 정상적으로 수정된다.")
    @Transactional
    void updateApplication() {
        // given
        Long classYearId = 1L;
        List<ApplicationAnswerDTO> answerList = createApplicationAnswerDTOList(2, "답변");
        ClassYear classYear = createClassYear(classYearId);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        Application application = new Application(member, createApplicationModel(answerList));
        application.updateClassYear(classYear);
        applicationRepository.save(application);

        String email = member.getEmail();
        String newTechStack = "Python";
        String newLinks = "https://new-link.com";
        Track newTrack = Track.FRONT_END;
        ApplicationStatus newApplicationStatus = ApplicationStatus.SAVED;
        List<ApplicationAnswerDTO> newAnswerList = createApplicationAnswerDTOList(2, "새로운 답변");

        ApplicationRequest applicationUpdateRequest = ApplicationRequest.builder()
                .classYearId(classYearId) // 기수 정보는 수정 불가
                .techStack(newTechStack)
                .links(newLinks)
                .track(newTrack)
                .applicationStatus(newApplicationStatus)
                .answers(newAnswerList)
                .build();
        // when
        Long applicationId = applicationService.updateApplication(email, applicationUpdateRequest);
        Application updatedApplication = applicationRepository.findById(applicationId).orElseThrow();
        // then
        assertThat(updatedApplication.getClassYear().getId()).isEqualTo(classYearId);
        assertThat(updatedApplication.getTechStack()).isEqualTo(newTechStack);
        assertThat(updatedApplication.getLinks()).isEqualTo(newLinks);
        assertThat(updatedApplication.getTrack()).isEqualTo(newTrack);
        assertThat(updatedApplication.getApplicationStatus()).isEqualTo(newApplicationStatus);
        assertThat(updatedApplication.getAnswers().size()).isEqualTo(newAnswerList.size());
        for (int i = 0; i < newAnswerList.size(); i++) {
            assertThat(updatedApplication.getAnswers().get(i).getQuestionNumber()).isEqualTo(newAnswerList.get(i).getQuestionNumber());
            assertThat(updatedApplication.getAnswers().get(i).getAnswer()).isEqualTo(newAnswerList.get(i).getAnswer());
        }
    }

    @Test
    @DisplayName("임시 저장 상태가 아닌 지원서를 수정할 경우 예외가 발생한다.")
    void FailedUpdateApplication() {
        // given
        Long classYearId = 1L;
        Application application = createApplication(null, Track.AI, ApplicationStatus.SAVED);
        ClassYear classYear = createClassYear(classYearId);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        application.updateClassYear(classYear);
        ApplicationModel applicationModel = ApplicationModel.builder()
                .techStack("Java")
                .links("https://github.com")
                .track(Track.BACK_END)
                .answers(new ArrayList<>())
                .applicationStatus(ApplicationStatus.SAVED)
                .build();
        application.updateApplication(member, applicationModel);
        applicationRepository.save(application);
        ApplicationRequest applicationRequest = createApplicationRequest(classYearId);
        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                applicationService.updateApplication(member.getEmail(), applicationRequest));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.APPLICATION_CONFLICT);
    }

    @Test
    @DisplayName("데드라인을 넘어서 지원서를 수정 시 예외가 발생한다.")
    void FailedUpdateApplicationDeadline() {
        // given
        Long classYearId = 1L;
        ApplicationStatus applicationStatus = ApplicationStatus.TEMPORAL;
        Application application = createApplication(null, Track.AI, applicationStatus);
        ClassYear classYear = createExpiredClassYear(1L);
        Member member = createMember(1L);
        classYearRepository.save(classYear);
        memberRepository.save(member);
        application.updateClassYear(classYear);
        application.updateApplication(member, createApplicationModel());
        applicationRepository.save(application);
        ApplicationRequest applicationRequest = createApplicationRequest(classYearId);
        // when
        CustomException customException = assertThrows(CustomException.class, () ->
                applicationService.updateApplication(member.getEmail(), applicationRequest));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.APPLICATION_DEADLINE_EXPIRED);
    }
}
