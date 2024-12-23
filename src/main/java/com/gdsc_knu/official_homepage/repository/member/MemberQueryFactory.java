package com.gdsc_knu.official_homepage.repository.member;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.util.List;

public interface MemberQueryFactory {
    List<Member> findAllByTrackAndRoleIn(Track track, List<Role> roles);
}
