package com.gdsc_knu.official_homepage.dto.admin.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
