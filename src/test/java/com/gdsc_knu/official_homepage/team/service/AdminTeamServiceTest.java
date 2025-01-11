package com.gdsc_knu.official_homepage.team.service;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamRequest;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.repository.team.MemberTeamRepository;
import com.gdsc_knu.official_homepage.repository.team.TeamRepository;
import com.gdsc_knu.official_homepage.service.admin.AdminTeamServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gdsc_knu.official_homepage.team.TeamTestEntityFactory.createMember;
import static com.gdsc_knu.official_homepage.team.TeamTestEntityFactory.createTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminTeamServiceTest {
    @InjectMocks private AdminTeamServiceImpl adminTeamService;
    @Mock private MemberRepository memberRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private MemberTeamRepository memberTeamRepository;

    @Test
    @DisplayName("상위팀 생성 시 track이 같은 모든 멤버는 해당 팀에 포함된다.")
    void createParentTeamAndConnectMembers() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        when(memberRepository.findAllByTrackAndRoleIn(any(), any())).thenReturn(new ArrayList<>(List.of(member1, member2)));
        String teamName = "팀 이름";
        // when
        adminTeamService.createParentTeam(new AdminTeamRequest.Create(teamName, null));
        // then
        assertThat(member1.getMemberTeams().size()).isEqualTo(1L);
        assertThat(member1.getMemberTeams().get(0).getTeam().getTeamName()).isEqualTo(teamName);
        assertThat(member1.getMemberTeams().get(0).getMember()).isEqualTo(member1);
        assertThat(member2.getMemberTeams().size()).isEqualTo(1L);
        assertThat(member2.getMemberTeams().get(0).getTeam().getTeamName()).isEqualTo(teamName);
        assertThat(member2.getMemberTeams().get(0).getMember()).isEqualTo(member2);
    }

    @Test
    @DisplayName("하위팀 생성시 상위팀과 연관관계가 정상적으로 매핑된다.")
    void createSubTeamAndCommentParent() {
        // given
        Team parent = createTeam(1L, null);
        when(teamRepository.findById(parent.getId())).thenReturn(Optional.of(parent));

        // when
        adminTeamService.createSubTeam(parent.getId());

        // then
        assertThat(parent.getSubTeams().size()).isEqualTo(1L);
        assertThat(parent.getSubTeams().get(0).getParent()).isEqualTo(parent);
        assertThat(parent.getSubTeams().get(0).getTeamName()).isEqualTo(String.format("%s 1팀", parent.getTeamName()));

    }


}
