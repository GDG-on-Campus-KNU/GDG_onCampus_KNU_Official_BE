package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByNameAndStudentNumber(String name, String studentNumber);
}
