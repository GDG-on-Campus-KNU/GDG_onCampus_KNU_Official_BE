package com.gdsc_knu.official_homepage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Team parent;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Team> subTeams = new ArrayList<>();


    public static Team fromParent(Team parent) {
        Team newSubTeam = Team.builder()
                .teamName(parent.getNewSubTeamName())
                .build();
        parent.addSubTeam(newSubTeam);
        return newSubTeam;
    }

    public static Team ofName(String teamName) {
        return Team.builder()
                .teamName(teamName)
                .build();
    }

    private void addSubTeam(Team subteam) {
        subTeams.add(subteam);
        subteam.parent = this;
    }

    private String getNewSubTeamName() {
        return String.format("%s %d팀", this.teamName, this.subTeams.size() + 1);
    }

    public Team getLastSubTeam() {
        int size = this.subTeams.size();
        return this.subTeams.get(size-1);
    }

    public void addMember(Member member) {
        MemberTeam memberTeam = MemberTeam.from(member, this);
        memberTeams.add(memberTeam);
        member.getMemberTeams().add(memberTeam);
    }

    public boolean isAssignedTeam() {
        return this.parent != null;
    }

}
