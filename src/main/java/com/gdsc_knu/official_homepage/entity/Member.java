package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
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

    private String profileUrl;

    private String introduction;

    @OneToMany(mappedBy = "member")
    private final List<MemberTeam> memberTeams = new ArrayList<>();
    public void updateRole(Role role) {
        this.role = role;
    }
    public void update(String name, String profileUrl, int age, String major, String studentNumber, String email, String introduction){
        this.name = name;
        this.profileUrl = profileUrl;
        this.age = age;
        this.major = major;
        this.studentNumber = studentNumber;
        this.email = email;
        this.introduction = introduction;
    }
    public void addInfo(String name, int age, String major, String studentNumber){
        this.name = name;
        this.age = age;
        this.major = major;
        this.studentNumber = studentNumber;
        this.role = Role.ROLE_MEMBER;
    }
}