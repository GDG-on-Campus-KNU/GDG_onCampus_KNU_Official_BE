package com.gdsc_knu.official_homepage.repository.team;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);

    @Query("SELECT mt " +
            "FROM MemberTeam mt " +
            "JOIN FETCH mt.member " +
            "WHERE mt.team.id = :teamId")
    List<MemberTeam> findAllByTeamId(@Param("teamId") Long teamId);

    List<MemberTeam> findAllMemberTeamByTeamId(Long teamId);
}
