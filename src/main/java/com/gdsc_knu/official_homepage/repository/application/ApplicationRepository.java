package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationQueryFactory{
    Optional<Application> findByNameAndStudentNumberAndClassYearId(String name, String studentNumber, Long classYearId);
    Optional<Application> findByStudentNumber(String studentNumber);

    @Query("SELECT " +
            "COUNT(CASE WHEN a.applicationStatus != 'TEMPORAL' THEN 1 END) AS total, " +
            "COUNT(CASE WHEN a.isOpened = true AND a.applicationStatus != 'TEMPORAL' THEN 1 END) AS openCount, " +
            "COUNT(CASE WHEN a.applicationStatus = 'APPROVED' THEN 1 END) AS approvedCount, " +
            "COUNT(CASE WHEN a.applicationStatus = 'REJECTED' THEN 1 END) AS rejectedCount " +
            "FROM Application a")
    ApplicationStatisticType getStatistics();


    @Query("SELECT a.track as track, COUNT(*) as count " +
            "FROM Application a " +
            "WHERE a.applicationStatus != 'TEMPORAL' " +
            "GROUP BY a.track")
    List<ApplicationTrackType> getGroupByTrack();

    Page<Application> findByNameContaining(Pageable pageable, String name);

    List<Application> findByApplicationStatusIn(List<ApplicationStatus> applicationStatus);

    List<Application> findByEmailIn(Set<Object> email);
}
