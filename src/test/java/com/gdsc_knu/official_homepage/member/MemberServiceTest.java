package com.gdsc_knu.official_homepage.member;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class MemberServiceTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

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
    @DisplayName("사용자의 팀 조회시 실제 배정되지 않은 팀은 조회되지 않는다.")
    void getMemberTeam() {

    }

    // TODO: 테스트 중 team 생성에 member - team 간 관계매핑이 원자적으로 이루어지지 않는것을 발견, 관련 로직 수정 필요
    @Test
    @DisplayName("사용자의 팀 조회시 최근순으로 조회된다.")
    void getMemberTeamOrder() {
        Team notAssignedParent = parentTeam();
        Team assignedParent = parentTeam();
        Team realAssigned = assignedTeam(assignedParent);


    }

    private Member createMember(Long id) {
        return Member.builder()
                .name("이름")
                .email(String.format("test%s@email.com", id))
                .track(Track.UNDEFINED)
                .role(Role.ROLE_TEMP)
                .build();
    }

    private Team parentTeam() {
        return Team.builder()
                .parent(null)
                .build();
    }

    private Team assignedTeam(Team parent) {
        Team team = Team.builder()
                .build();
        parent.addSubTeam(team);
        return team;
    }

    private MemberTeam createMemberTeam(Member member, Team team) {
        return  MemberTeam.builder()
                .member(member)
                .team(team)
                .build();
    }

}
