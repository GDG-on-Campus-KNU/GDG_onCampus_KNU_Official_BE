package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
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

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileUrl;

    @Column(length = 500)
    private String introduction;

    private Track track;

    @OneToMany(mappedBy = "member")
    private final List<MemberTeam> memberTeams = new ArrayList<>();
    public void updateRole(Role role) {
        this.role = role;
    }
    public void update(String name, int age, String major, String studentNumber, String phoneNumber, String introduction){
        this.name = name;
        this.age = age;
        this.major = major;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
    }
    public void addInfo(String name, int age, String major, String studentNumber, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.major = major;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.role = Role.ROLE_GUEST;
    }
}