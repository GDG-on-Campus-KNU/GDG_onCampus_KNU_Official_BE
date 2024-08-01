package com.gdsc_knu.official_homepage.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoResponse {
    private String teamName;
    private String teamPageUrl;
}