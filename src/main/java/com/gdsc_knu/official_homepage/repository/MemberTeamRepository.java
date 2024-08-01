package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);

    Optional<MemberTeam> findAllByTeamId(Long teamId);
}
