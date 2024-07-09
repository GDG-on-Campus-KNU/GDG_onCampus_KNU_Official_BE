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
    @NotBlank(message = "이름은 필수 입력 필드입니다.")
    private String name;

    @NotBlank(message = "학번은 필수 입력 필드입니다.")
    private String studentNumber;

    @NotBlank(message = "전공은 필수 입력 필드입니다.")
    private String major;

    @Schema(description = "이메일", example = "aaa@aaa.com")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(description = "휴대폰 번호", example = "010-0000-0000")
    @PhoneNumber
    private String phoneNumber;

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
                .answers(answers.stream()
                        .map(answer -> ApplicationAnswer.builder()
                                .questionNumber(answer.getQuestionNumber())
                                .answer(answer.getAnswer())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
