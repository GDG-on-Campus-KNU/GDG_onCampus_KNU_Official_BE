package com.gdsc_knu.official_homepage.member.repository;

import com.gdsc_knu.official_homepage.config.QueryDslConfig;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("직렬별 사용자를 이름으로 정렬하여 조회한다. MEMBER 만 조회된다.")
    void findAllByTrackOrderByName() {
        // given
        Member member1 = createMember(1L,"하철수",Track.AI, Role.ROLE_MEMBER);
        Member member2 = createMember(2L, "가철수",Track.AI, Role.ROLE_MEMBER);
        Member member3 = createMember(3L, "나철수",Track.BACK_END, Role.ROLE_MEMBER);
        Member member4 = createMember(4L, "다철수",Track.AI, Role.ROLE_MEMBER);
        Member member5 = createMember(5L, "라철수",Track.AI, Role.ROLE_CORE);
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5));

        // when
        List<Member> memberList = memberRepository.findAllByTrackOrderByName(Track.AI);

        // then
        assertThat(memberList).extracting("name")
                .containsExactly(
                        "가철수", "다철수", "하철수"
                );
        assertThat(memberList).allSatisfy(member ->
                assertThat(member.getTrack()).isEqualTo(Track.AI)
        );
    }

    @Test
    @DisplayName("직렬, 권한으로 필터링하여 사용자를 조회한다.")
    void findAllByTrackAndRoleIn() {
        // given
        Member member1 = createMember(1L,"가철수",Track.AI, Role.ROLE_MEMBER);
        Member member2 = createMember(2L, "나철수",Track.AI, Role.ROLE_MEMBER);
        Member member3 = createMember(3L, "다철수",Track.BACK_END, Role.ROLE_MEMBER);
        Member member4 = createMember(4L, "라철수",Track.AI, Role.ROLE_MEMBER);
        Member member5 = createMember(5L, "마철수",Track.AI, Role.ROLE_CORE);
        Member member6 = createMember(6L, "바철수",Track.AI, Role.ROLE_GUEST);
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5, member6));

        // when
        List<Member> memberList = memberRepository.findAllByTrackAndRoleIn(Track.AI, List.of(Role.ROLE_MEMBER, Role.ROLE_CORE));

        // then
        assertThat(memberList).hasSize(4);
        assertThat(memberList).noneSatisfy(member -> {
            assertThat(member.getTrack()).isEqualTo(Track.BACK_END);
            assertThat(member.getRole()).isEqualTo(Role.ROLE_GUEST);
        });
    }

    private Member createMember(Long id, String name, Track track, Role role) {
        return Member.builder()
                .name(name)
                .email(String.format("test%s@email.com", id))
                .track(track)
                .role(role)
                .build();
    }
}
