package com.gdsc_knu.official_homepage.entity.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequestDTO;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private String name;

    private String studentNumber;

    private String major;

    private String email;

    private String phoneNumber;

    private String techStack;

    private String links;

    private LocalDateTime submittedAt;

    @ManyToOne
    @JoinColumn(name = "class_year_id")
    private ClassYear classYear;

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

    public Application(Member member, ApplicationRequestDTO applicationRequestDTO) {
        this.name = member.getName();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.techStack = applicationRequestDTO.getTechStack();
        this.links = applicationRequestDTO.getLinks();
        this.applicationStatus = applicationRequestDTO.getApplicationStatus();
        this.track = applicationRequestDTO.getTrack();
        this.answers = applicationRequestDTO.getAnswers().stream()
                .map(answers -> ApplicationAnswer.builder()
                        .questionNumber(answers.getQuestionNumber())
                        .answer(answers.getAnswer())
                        .build()).toList();
        this.submittedAt = LocalDateTime.now();
    }

    public void updateApplication(Member member, ApplicationRequestDTO applicationRequestDTO) {
        this.name = member.getName();
        this.studentNumber = member.getStudentNumber();
        this.major = member.getMajor();
        this.email = member.getEmail();
        this.phoneNumber = member.getPhoneNumber();
        this.techStack = applicationRequestDTO.getTechStack();
        this.links = applicationRequestDTO.getLinks();
        this.applicationStatus = applicationRequestDTO.getApplicationStatus();
        this.track = applicationRequestDTO.getTrack();
        updateNewAnswers(applicationRequestDTO.getAnswers());
        this.submittedAt = LocalDateTime.now();
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

    public void updateClassYear(ClassYear classYear) {
        this.classYear = classYear;
    }
}
