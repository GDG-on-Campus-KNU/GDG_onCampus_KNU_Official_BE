package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberRoleRequest {
    private List<Long> userIds;
    private Role role;
}
