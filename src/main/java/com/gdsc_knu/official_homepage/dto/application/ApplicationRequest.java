package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationRequest {
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

    private Map<Integer, String> answers;

    public Application toEntity() {
        if (this.applicationStatus == null) {
            this.applicationStatus = ApplicationStatus.TEMPORAL;
        }
        return Application.builder()
                .name(name)
                .studentNumber(studentNumber)
                .major(major)
                .email(email)
                .phoneNumber(phoneNumber)
                .techStack(techStack)
                .links(links)
                .applicationStatus(applicationStatus)
                .track(track)
                .answers(this.answers.entrySet().stream()
                        .map(entry -> ApplicationAnswer.builder()
                                .questionNumber(entry.getKey())
                                .answer(entry.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
