package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.OfficialHomepageApplication;
import com.gdsc_knu.official_homepage.config.QueryDslConfig;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
@ContextConfiguration(classes = OfficialHomepageApplication.class)
public class AdminApplicationRepositoryTest {
    @Autowired private ApplicationRepository applicationRepository;

    @AfterEach
    void clear() {
        applicationRepository.deleteAll();
    }


    @Test
    @DisplayName("지원 현황을 정상적으로 카운트한다.")
    void getStatistic() {
        // given
        int start = 1;
        int countPerStatus = 5;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, ApplicationStatus.TEMPORAL);
        start+=countPerStatus;
        List<Application> save = createApplicationList(start, start+countPerStatus, ApplicationStatus.SAVED);
        start+=countPerStatus;
        List<Application> approve = createApplicationList(start, start+countPerStatus, ApplicationStatus.APPROVED);
        start+=countPerStatus;
        List<Application> reject = createApplicationList(start, start+countPerStatus, ApplicationStatus.REJECTED);
        List<Application> allApplications = Stream.of(temporal, save, approve, reject)
                .flatMap(List::stream)
                .toList();
        applicationRepository.saveAll(allApplications);

        // when
        ApplicationStatisticType statistic = applicationRepository.getStatistics();

        // then
        assertThat(statistic.getTotal()).isEqualTo(15);
        assertThat(statistic.getApprovedCount()).isEqualTo(5);
        assertThat(statistic.getRejectedCount()).isEqualTo(5);
        assertThat(statistic.getOpenCount()).isEqualTo(0);
    }

    private List<Application> createApplicationList(int startNum, int count, ApplicationStatus status){
        List<Application> applicationList = new ArrayList<>();
        for (int i=startNum; i<count; i++) {
            applicationList.add(createApplication(i, status));
        }
        return applicationList;
    }

    private Application createApplication(int id, ApplicationStatus status) {
        return Application.builder()
                .email(String.format("test%s@email.com", id))
                .studentNumber(String.valueOf(id))
                .phoneNumber(String.format("010-0000-%s", id))
                .applicationStatus(status)
                .build();
    }
}
