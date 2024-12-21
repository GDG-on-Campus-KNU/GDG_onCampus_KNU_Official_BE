package com.gdsc_knu.official_homepage.dto.admin.application;

import com.gdsc_knu.official_homepage.entity.ClassYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminApplicationRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClassYearRequest {
        private String name;
        private LocalDateTime applyStartDateTime;
        private LocalDateTime applyEndDateTime;

        public ClassYear toEntity() {
            return ClassYear.builder()
                    .name(name)
                    .applicationStartDateTime(applyStartDateTime)
                    .applicationEndDateTime(applyEndDateTime)
                    .build();
        }
    }
}
