package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.dto.MemberInfoRequest;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    private String studentNumber;

    private String major;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String sessionId;

    private String profileUrl;

    private String introduction;

    @OneToMany(mappedBy = "member")
    private final List<MemberTeam> memberTeams = new ArrayList<>();
    public void updateSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void updateRole(Role role) {
        this.role = role;
    }
    public void update(MemberInfoRequest memberInfoRequest){
        this.name = memberInfoRequest.getName();
        this.profileUrl = memberInfoRequest.getProfileUrl();
        this.age = memberInfoRequest.getAge();
        this.major = memberInfoRequest.getMajor();
        this.studentNumber = memberInfoRequest.getStudentNumber();
        this.email = memberInfoRequest.getEmail();
        this.introduction = memberInfoRequest.getIntroduction();
    }
}