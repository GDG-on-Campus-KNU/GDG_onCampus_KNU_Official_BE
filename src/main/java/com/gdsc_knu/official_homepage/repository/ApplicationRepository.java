package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByNameAndStudentNumber(String name, String studentNumber);
    Optional<Application> findByStudentNumber(String studentNumber);

    @Query("SELECT " +
            "COUNT(*) AS total, " +
            "COUNT(CASE WHEN a.isOpened = true THEN 1 ELSE 0 END) AS openCount, " +
            "COUNT(CASE WHEN a.applicationStatus = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount " +
            "FROM Application a")
    ApplicationStatisticType getStatistics();

    Page<Application> findAllByApplicationStatus(Pageable pageable, ApplicationStatus applicationStatus);
}
