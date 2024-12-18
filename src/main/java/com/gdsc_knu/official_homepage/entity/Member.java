package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int age;

    private String studentNumber;

    private String major;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 500)
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Track track;

    @OneToMany(mappedBy = "member")
    private final List<MemberTeam> memberTeams = new ArrayList<>();

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateTrack(Track track) {
        this.track = track;
    }
  
    public void addInfo(String name, int age, String major, String studentNumber, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.major = major;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.role = Role.ROLE_GUEST;
    }

    public List<Team> getTeams() {
        return this.memberTeams.stream()
                .map(MemberTeam::getTeam)
                .filter(team -> team.getParent()!=null)
                .toList();
    }
}