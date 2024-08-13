package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationQueryFactory{
    Optional<Application> findByNameAndStudentNumber(String name, String studentNumber);
    Optional<Application> findByStudentNumber(String studentNumber);

    @Query("SELECT " +
            "COUNT(CASE WHEN a.applicationStatus != 'TEMPORAL' THEN 1 END) AS total, " +
            "COUNT(CASE WHEN a.isOpened = true THEN 1 END) AS openCount, " +
            "COUNT(CASE WHEN a.applicationStatus = 'APPROVED' THEN 1 END) AS approvedCount " +
            "FROM Application a")
    ApplicationStatisticType getStatistics();

    Page<Application> findByNameContaining(Pageable pageable, String name);
}
