package com.gdsc_knu.official_homepage.member.service;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.repository.team.TeamRepository;
import com.gdsc_knu.official_homepage.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class MemberServiceTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamRepository teamRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("중복된 전화번호로 회원가입하는 경우 반영되지 않고, GUEST 권한을 가질 수 없다.")
    void GetGuestAuthorityFail() {
        // when
        String duplicatePhoneNumber = "010-0000-0000";
        Member existingMember = createMember(1L);
        existingMember.addInfo("기존멤버",20,"컴퓨터전공","20200000", duplicatePhoneNumber);
        memberRepository.save(existingMember);

        Member newMember = createMember(2L);
        memberRepository.save(newMember);
        MemberRequest.Append request =
                new MemberRequest.Append("신규멤버", 20, "컴퓨터전공","20200001", duplicatePhoneNumber);
        // when
        assertThrows(DataIntegrityViolationException.class, () ->
                memberService.addMemberInfo(newMember.getId(), request));
        // then
        assertThat(newMember.getRole()).isEqualTo(Role.ROLE_TEMP);
    }


    @Test
    @DisplayName("사용자의 팀 조회시 실제 배정되지 않은 팀(편의상 부모팀)은 조회되지 않는다.")
    void getMemberTeam() {
        // given
        Member member = createMember(1L);
        memberRepository.save(member);
        String parentName = "미배정부모팀", assignedName = "배정 1팀";
        Team parent = parentTeam(1L, parentName);
        Team assignedTeam = assignedTeam(2L, parent, assignedName);
        for (Team team : List.of(parent, assignedTeam)){
            team.addMember(member);
            teamRepository.save(team);
        }
        // when
        List<TeamResponse.Main> response =  memberService.getMemberTeamInfo(member.getId());
        // then
        assertThat(response.size()).isEqualTo(1L);
        assertThat(response.get(0).getTeamName()).isEqualTo(assignedName);
    }

    @Test
    @DisplayName("사용자의 팀 조회시 최근순으로 조회된다.")
    void getMemberTeamOrder() {
        // given
        Member member = createMember(1L);
        memberRepository.save(member);

        String teamName = "테스트";
        Team parent1 = parentTeam(1L, teamName);
        Team parent2 = parentTeam(2L, teamName);
        Team child1 = assignedTeam(3L, parent1,teamName);
        Team child2 = assignedTeam(4L, parent1,teamName);
        Team child3 = assignedTeam(5L, parent2,teamName);
        for (Team team : List.of(parent1, parent2, child1, child2, child3)){
            team.addMember(member);
            teamRepository.save(team);
        }
        // when
        List<TeamResponse.Main> response =  memberService.getMemberTeamInfo(member.getId());
        // then
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.stream().map(TeamResponse.Main::getId)).containsExactly(5L, 4L, 3L);
    }

    private Member createMember(Long id) {
        return Member.builder()
                .name("이름")
                .email(String.format("test%s@email.com", id))
                .track(Track.UNDEFINED)
                .role(Role.ROLE_TEMP)
                .build();
    }

    private Team parentTeam(Long id, String teamName) {
        return Team.builder()
                .id(id)
                .teamName(teamName)
                .parent(null)
                .build();
    }

    private Team assignedTeam(Long id, Team parent, String teamName) {
        return Team.builder()
                .id(id)
                .parent(parent)
                .teamName(teamName)
                .build();
    }
}
