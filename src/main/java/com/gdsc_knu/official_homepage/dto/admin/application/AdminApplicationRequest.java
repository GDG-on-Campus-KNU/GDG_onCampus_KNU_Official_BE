package com.gdsc_knu.official_homepage.dto.admin.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminApplicationRequest {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Append {
        private String note;
        private Integer version;
    }
}
