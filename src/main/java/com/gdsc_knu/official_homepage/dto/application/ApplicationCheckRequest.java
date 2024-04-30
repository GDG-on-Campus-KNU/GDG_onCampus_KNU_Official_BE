package com.gdsc_knu.official_homepage.dto.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApplicationCheckRequest {
    private String name;
    private String studentNumber;
}
