package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberDeleteRequest {
    private List<Long> userIds;
}
