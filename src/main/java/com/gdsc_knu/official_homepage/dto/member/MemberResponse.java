package com.gdsc_knu.official_homepage.dto.member;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private String name;
    private String profileUrl;
    private int age;
    private String major;
    private String studentNumber;
    private String email;
    private Role role;
    private List<TeamInfoResponse> teamInfos;
    public MemberResponse(Member member, List<TeamInfoResponse> teamInfos){
        this.name = member.getName();
        this.profileUrl = member.getProfileUrl();
        this.age = member.getAge();
        this.major = member.getMajor();
        this.studentNumber = member.getStudentNumber();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.teamInfos = teamInfos;
    }
}
