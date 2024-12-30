package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationQueryFactory {
    Page<Application> findAllApplicationsByOption(Pageable pageable, Track track, Boolean isMarked, Long classYearId);

    ApplicationStatisticType getStatistics(Long classYearId);

    List<ApplicationTrackType> getGroupByTrack(Long classYearId);
}
