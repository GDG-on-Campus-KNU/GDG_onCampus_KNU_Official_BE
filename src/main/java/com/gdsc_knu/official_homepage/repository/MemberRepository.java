package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
