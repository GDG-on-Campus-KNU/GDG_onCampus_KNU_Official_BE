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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;

    @Column(length = 1024)
    private String answer;

    @ManyToOne
    private Application application;

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
