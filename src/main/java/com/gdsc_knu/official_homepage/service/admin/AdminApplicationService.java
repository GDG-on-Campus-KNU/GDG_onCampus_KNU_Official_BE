package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRes;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
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

}
