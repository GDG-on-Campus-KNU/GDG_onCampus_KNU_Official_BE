package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.entity.application.Application;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime applicationStartDateTime;

    @Column(nullable = false)
    private LocalDateTime applicationEndDateTime;

    public void update(String name, LocalDateTime applicationStartDateTime, LocalDateTime applicationEndDateTime) {
        this.name = name;
        this.applicationStartDateTime = applicationStartDateTime;
        this.applicationEndDateTime = applicationEndDateTime;
    }
}
