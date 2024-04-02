package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
