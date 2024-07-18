package com.gdsc_knu.official_homepage.dto.admin.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminApplicationRes {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {
        private Integer total;
        private Integer openCount;
        private Integer notOpenCount;
        private Integer approvedCount;
        private Integer rejectedCount;

        public static Statistics of(int total, int openCount, int notOpenCount, int approvedCount, int rejectedCount) {
            return Statistics.builder()
                    .total(total)
                    .openCount(openCount)
                    .notOpenCount(notOpenCount)
                    .approvedCount(approvedCount)
                    .rejectedCount(rejectedCount)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Overview {
        private Long id;
        private String name;
        private LocalDateTime submittedAt;
        private String studentNumber;
        private String major;
        private String track;
        private boolean isOpen;

        public static Overview from(Application application){
            return Overview.builder()
                    .id(application.getId())
                    .name(application.getName())
                    .submittedAt(application.getModifiedAt())
                    .studentNumber(application.getStudentNumber())
                    .major(application.getMajor())
                    .track(application.getTrack().name())
                    .isOpen(application.isOpened())
                    .build();
        }
    }
}
