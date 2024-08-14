package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberRoleUpdateRequest {
    private List<Long> userIds;
    private Role role;
}
