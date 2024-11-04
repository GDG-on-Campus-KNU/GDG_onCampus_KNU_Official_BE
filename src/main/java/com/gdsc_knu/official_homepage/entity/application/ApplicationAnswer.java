package com.gdsc_knu.official_homepage.entity.application;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int questionNumber;

    @Column(length = 1024)
    private String answer;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Application application;

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}
