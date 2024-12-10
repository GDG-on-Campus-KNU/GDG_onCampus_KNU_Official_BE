package com.gdsc_knu.official_homepage.repository.member;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryFactory {
    @Query("SELECT m " +
           "FROM Member m " +
           "JOIN FETCH m.memberTeams mt " +
           "JOIN FETCH mt.team " +
           "WHERE m.id = :id")
    Optional<Member> findByIdWithTeam(Long id);

    Member findByEmailAndStudentNumber(String email, String studentNumber);

    Member getByEmail(String email);
    Optional<Member> findByEmail(String Email);

    Optional<Member> findByName(String name);

    @Query("SELECT m FROM " +
            "Member m " +
            "WHERE m.track =:track " +
            "AND m.role = 'ROLE_MEMBER' " +
            "ORDER BY m.name")
    List<Member> findAllByTrackOrderByName(Track track);
  
    @Modifying
    @Query("delete from Member m where m.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);

    Page<Member> findByNameContaining(Pageable pageable, String name);
}
