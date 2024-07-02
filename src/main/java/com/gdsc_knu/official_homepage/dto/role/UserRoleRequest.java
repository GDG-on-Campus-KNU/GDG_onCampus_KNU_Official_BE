package com.gdsc_knu.official_homepage.dto.role;

import lombok.Getter;

import java.util.List;

@Getter
public class UserRoleRequest {
    private List<Long> userIds;
}
