package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRes;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public AdminApplicationRes.Statistics getStatistic() {
        ApplicationStatisticType statistic = applicationRepository.getStatistics();
        return AdminApplicationRes.Statistics.of(
                statistic.getTotal(),
                statistic.getOpenCount(),
                statistic.getTotal() - statistic.getOpenCount(),
                statistic.getApprovedCount(),
                statistic.getTotal() - statistic.getApprovedCount());
    }

    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationRes.Overview> getAllApplications(int page, int size, boolean isMarked){
        Page<Application> applicationPage = isMarked
                ? applicationRepository.findAllByIsMarked(PageRequest.of(page,size))
                : applicationRepository.findAllSummited(PageRequest.of(page,size));
        return PagingResponse.from(applicationPage, AdminApplicationRes.Overview::from);
    }

    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationRes.Overview> getAllApplicationsByOption(int page, int size, Track track, boolean isMarked){
        Page<Application> applicationPage = isMarked
                ? applicationRepository.findAllByTrackAndIsMarked(PageRequest.of(page,size), track)
                : applicationRepository.findAllByTrack(PageRequest.of(page,size), track);

        return PagingResponse.from(applicationPage, AdminApplicationRes.Overview::from);
    }
}
