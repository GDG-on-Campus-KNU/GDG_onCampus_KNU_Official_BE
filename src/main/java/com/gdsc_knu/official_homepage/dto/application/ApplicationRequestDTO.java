package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ApplicationRequestDTO {
    private String techStack;
    private String links;
    private ApplicationStatus applicationStatus;
    private Track track;
    private List<ApplicationAnswerDTO> answers;
}
