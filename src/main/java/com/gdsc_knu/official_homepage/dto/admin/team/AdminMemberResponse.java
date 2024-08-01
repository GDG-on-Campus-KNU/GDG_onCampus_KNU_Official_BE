package com.gdsc_knu.official_homepage.dto.admin.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMemberResponse {
    private Long id;
    private String name;
    private String studentNumber;
}
