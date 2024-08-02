package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationQueryFactory{
    Optional<Application> findByNameAndStudentNumber(String name, String studentNumber);
    Optional<Application> findByStudentNumber(String studentNumber);

    @Query("SELECT " +
            "COUNT(CASE WHEN a.applicationStatus = 'SAVED' THEN 1 ELSE 0 END) AS total, " +
            "COUNT(CASE WHEN a.isOpened = true THEN 1 ELSE 0 END) AS openCount, " +
            "COUNT(CASE WHEN a.applicationStatus = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount " +
            "FROM Application a")
    ApplicationStatisticType getStatistics();

    @Query("SELECT a FROM Application a " +
            "WHERE a.applicationStatus = 'SAVED' " +
            "AND a.track = :track " +
            "AND a.isMarked = true")
    Page<Application> findAllByTrackAndIsMarked(Pageable pageable, @Param("track")Track track);

    @Query("SELECT a FROM Application a " +
            "WHERE a.applicationStatus = 'SAVED' " +
            "AND a.track = :track")
    Page<Application> findAllByTrack(Pageable pageable, @Param("track")Track track);


    @Query("SELECT a FROM Application a " +
            "WHERE a.applicationStatus = 'SAVED' " +
            "AND a.isMarked = true")
    Page<Application> findAllByIsMarked(Pageable pageable);

    @Query("SELECT a FROM Application a " +
            "WHERE a.applicationStatus = 'SAVED'")
    Page<Application> findAllSummited(Pageable pageable);
}
