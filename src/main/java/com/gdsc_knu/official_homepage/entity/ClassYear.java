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

    private String name;

    private LocalDateTime applicationStartDateTime;

    private LocalDateTime applicationEndDateTime;
}
