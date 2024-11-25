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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@Import(QueryDslConfig.class)
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("직렬별 사용자를 이름으로 정렬하여 조회한다. MEMBER 만 조회된다.")
    void findAllByTrackOrderByName() {
        // given
        Member member1 = createMember(1L,"하철수",Track.AI);
        Member member2 = createMember(2L, "가철수",Track.AI);
        Member member3 = createMember(3L, "나철수",Track.BACK_END);
        Member member4 = createMember(4L, "다철수",Track.AI);
        Member member5 = createMember(5L, "라철수",Track.AI);
        ReflectionTestUtils.setField(member5, "role", Role.ROLE_CORE);
        memberRepository.saveAll(List.of(member1, member2, member3, member4));

        // when
        List<Member> memberList = memberRepository.findAllByTrackOrderByName(Track.AI);

        // then
        assertThat(memberList).extracting("id","name")
                .containsExactly(
                        tuple(2L, "가철수"),
                        tuple(4L, "다철수"),
                        tuple(1L, "하철수")
                );
        assertThat(memberList).allSatisfy(member ->
                assertThat(member.getTrack()).isEqualTo(Track.AI)
        );
    }

    private Member createMember(Long id, String name, Track track) {
        return Member.builder()
                .name(name)
                .email(String.format("test%s@email.com", id))
                .track(track)
                .role(Role.ROLE_MEMBER)
                .build();
    }
}
