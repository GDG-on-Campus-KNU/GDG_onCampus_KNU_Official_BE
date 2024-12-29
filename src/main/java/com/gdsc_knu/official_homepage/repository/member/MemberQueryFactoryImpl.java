package com.gdsc_knu.official_homepage.repository.member;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.QMember;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberQueryFactoryImpl implements MemberQueryFactory{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Member> findAllByTrackAndRoleIn(Track track, List<Role> roles) {
        return jpaQueryFactory.selectFrom(QMember.member)
                .where(QMember.member.role.in(roles)
                        .and(eqTrack(track)))
                .fetch();
    }
    private BooleanExpression eqTrack(Track track) {
        return track == null ? null : QMember.member.track.eq(track);
    }
}
