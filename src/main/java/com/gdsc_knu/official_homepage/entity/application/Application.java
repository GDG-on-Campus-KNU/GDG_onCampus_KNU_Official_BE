package com.gdsc_knu.official_homepage.entity.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String name;

    @Column(unique = true)
    private String studentNumber;

    private String major;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String techStack;

    private String links;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Builder.Default
    private boolean isOpened = false;

    @Builder.Default
    private boolean isMarked = false;

    @Enumerated(EnumType.STRING)
    private Track track;

    @Column(length = 2048)
    private String note;

    @Builder.Default
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationAnswer> answers = new ArrayList<>();

    // TODO : presentation 계층과 의존 제거
    public Application(Member member, ApplicationRequest applicationRequest) {
        this.name = member.getName();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.techStack = applicationRequest.getTechStack();
        this.links = applicationRequest.getLinks();
        this.applicationStatus = applicationRequest.getApplicationStatus();
        this.track = applicationRequest.getTrack();
        this.answers = applicationRequest.getAnswers().stream()
                .map(answers -> ApplicationAnswer.builder()
                        .questionNumber(answers.getQuestionNumber())
                        .answer(answers.getAnswer())
                        .build()).toList();
    }

    public void updateApplication(Member member, ApplicationRequest applicationRequest) {
        this.name = member.getName();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.techStack = applicationRequest.getTechStack();
        this.links = applicationRequest.getLinks();
        this.applicationStatus = applicationRequest.getApplicationStatus();
        this.track = applicationRequest.getTrack();
        updateNewAnswers(applicationRequest.getAnswers());
    }

    private void updateNewAnswers(List<ApplicationAnswerDTO> newAnswersDTO) {
        Map<Integer, String> newAnswers = newAnswersDTO.stream()
                .collect(Collectors.toMap(ApplicationAnswerDTO::getQuestionNumber, ApplicationAnswerDTO::getAnswer));
        Map<Integer, ApplicationAnswer> oldAnswers = this.answers.stream()
                .collect(Collectors.toMap(ApplicationAnswer::getQuestionNumber, answer -> answer));
        int answerSize = oldAnswers.size();
        if (answerSize != newAnswers.size()) {
            throw new IllegalArgumentException("지원서의 질문 응답 개수가 일치하지 않습니다.");
        }
        for (int i = 0; i < answerSize; i++) {
            if (!oldAnswers.get(i).getAnswer().equals(newAnswers.get(i))) {
                oldAnswers.get(i).updateAnswer(newAnswers.get(i));
            }
        }
    }

    public void open() {
        this.isOpened = true;
    }

    public void changeMark() {
        this.isMarked = !this.isMarked;
    }

    public void updateStatus(ApplicationStatus status) {
        if (this.applicationStatus != ApplicationStatus.TEMPORAL){
            this.applicationStatus = status;
        }
    }

    public void saveNote(String note, Integer version) {
        if (this.version != version)
            throw new CustomException(ErrorCode.CONCURRENT_FAILED);
        this.note = note;
    }
}
