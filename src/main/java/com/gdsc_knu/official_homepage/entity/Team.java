package com.gdsc_knu.official_homepage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;
    private String teamPageUrl;

    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Team parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Team> subTeams = new ArrayList<>();

    public void addSubTeam(Team subteam) {
        subTeams.add(subteam);
        subteam.setParent(this);
    }

    public void setParent(Team parent) {
        this.parent = parent;
    }
}
