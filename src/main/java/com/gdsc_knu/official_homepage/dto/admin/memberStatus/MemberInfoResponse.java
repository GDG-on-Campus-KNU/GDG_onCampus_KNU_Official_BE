package com.gdsc_knu.official_homepage.dto.admin.memberStatus;

import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.util.List;

public class MemberInfoResponse {
    private Long Id;
    private Track track;
    private String name;
    private String studentNumber;
    private String email;
    private String phoneNumber;
    private List<Team> teams;
    private Role role;
}
