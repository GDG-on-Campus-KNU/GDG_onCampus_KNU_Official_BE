package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.entity.ClassYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassYearRepository extends JpaRepository<ClassYear, Long> {
    Optional<ClassYear> findByName(String name);
}
