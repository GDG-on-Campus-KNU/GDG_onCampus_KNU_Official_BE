package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationQueryFactory {
    Page<Application> findAllApplicationsByOption(Pageable pageable, Track track, boolean isMarked);
}
