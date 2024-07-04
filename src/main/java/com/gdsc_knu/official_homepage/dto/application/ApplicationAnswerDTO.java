package com.gdsc_knu.official_homepage.dto.application;

import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationAnswerDTO {
    private int questionNumber;
    private String answer;

    public ApplicationAnswerDTO(ApplicationAnswer applicationAnswer) {
        this.questionNumber = applicationAnswer.getQuestionNumber();
        this.answer = applicationAnswer.getAnswer();
    }
}
