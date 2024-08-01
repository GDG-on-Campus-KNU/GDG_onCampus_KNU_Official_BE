package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDeleteRequest {
    private List<Long> userIds;
}
