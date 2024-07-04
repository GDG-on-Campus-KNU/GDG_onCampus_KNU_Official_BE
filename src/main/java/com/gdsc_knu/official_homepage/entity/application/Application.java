package com.gdsc_knu.official_homepage.entity.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Application extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String studentNumber;

    private String major;

    private String email;

    private String phoneNumber;

    private String techStack;

    private String links;

    private ApplicationStatus applicationStatus;

    private Track track;

    public void updateApplication(ApplicationRequest applicationRequest) {
        this.name = applicationRequest.getName();
        this.studentNumber = applicationRequest.getStudentNumber();
        this.major = applicationRequest.getMajor();
        this.email = applicationRequest.getEmail();
        this.phoneNumber = applicationRequest.getPhoneNumber();
        this.techStack = applicationRequest.getTechStack();
        this.links = applicationRequest.getLinks();
        this.applicationStatus = applicationRequest.getApplicationStatus();
        this.track = applicationRequest.getTrack();
    }
}
