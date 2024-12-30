package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationQueryFactory {
    Optional<Application> findByNameAndStudentNumberAndClassYearId(String name, String studentNumber, Long classYearId);

    @Query("SELECT a " +
            "FROM Application a LEFT JOIN FETCH a.answers "+
            "WHERE a.id=:id")
    Optional<Application> findByIdFetchJoin(Long id);

    Page<Application> findByNameContaining(Pageable pageable, String name);

    List<Application> findByEmailIn(Set<Object> email);
}
