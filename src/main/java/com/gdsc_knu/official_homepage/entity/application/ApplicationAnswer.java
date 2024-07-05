package com.gdsc_knu.official_homepage.entity.application;

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
public class ApplicationAnswer {
    @Id
    @Column(name = "application_answer_id")
    @GeneratedValue
    private Long id;

    private int questionNumber;

    @Column(columnDefinition = "varchar(1024)")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
