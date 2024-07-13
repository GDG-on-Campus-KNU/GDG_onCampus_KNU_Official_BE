package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.annotation.PhoneNumber;
import com.gdsc_knu.official_homepage.annotation.ValidEnum;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ApplicationRequest {
    @NotNull(message = "잘못된 입력 형식입니다.")
    private String techStack;

    @NotNull(message = "잘못된 입력 형식입니다.")
    private String links;

    @Enumerated(EnumType.STRING)
    @ValidEnum(enumClass = ApplicationStatus.class, message = "올바르지 않은 지원상태입니다.")
    private ApplicationStatus applicationStatus;

    @Enumerated(EnumType.STRING)
    @ValidEnum(enumClass = Track.class, message = "올바르지 않은 트랙입니다.")
    private Track track;

    @NotNull(message = "잘못된 입력 형식입니다.")
    private List<ApplicationAnswerDTO> answers;

}
