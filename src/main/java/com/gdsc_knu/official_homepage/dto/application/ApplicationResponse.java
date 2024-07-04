package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationResponse {
    private Long id;

    private String name;

    private String studentNumber;

    private String major;

    private String email;

    private String phoneNumber;

    private String techStack;

    private String links;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Enumerated(EnumType.STRING)
    private Track track;

    private List<String> answers;

    public ApplicationResponse(Application application) {
        this.id = application.getId();
        this.name = application.getName();
        this.studentNumber = application.getStudentNumber();
        this.major = application.getMajor();
        this.email = application.getEmail();
        this.phoneNumber = application.getPhoneNumber();
        this.techStack = application.getTechStack();
        this.links = application.getLinks();
        this.applicationStatus = application.getApplicationStatus();
        this.track = application.getTrack();
        this.answers = application.getAnswers().stream()
                .map(ApplicationAnswer::getAnswer)
                .collect(Collectors.toList());
    }
}
