package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.entity.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Application toEntity() {
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
                .build();
    }
}
