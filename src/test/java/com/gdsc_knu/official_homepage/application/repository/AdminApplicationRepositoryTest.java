package com.gdsc_knu.official_homepage.application.repository;

import com.gdsc_knu.official_homepage.ClearDatabase;
import com.gdsc_knu.official_homepage.config.QueryDslConfig;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.application.ClassYearRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.*;
import static com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus.*;
import static com.gdsc_knu.official_homepage.entity.enumeration.Track.AI;
import static com.gdsc_knu.official_homepage.entity.enumeration.Track.BACK_END;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({QueryDslConfig.class, ClearDatabase.class})
public class AdminApplicationRepositoryTest {
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private ClassYearRepository classYearRepository;
    @Autowired private ClearDatabase clearDatabase;

    @AfterEach
    @Transactional
    @Rollback
    void tearDown() {
        clearDatabase.each("application");
        clearDatabase.each("class_year");
    }

    @Test
    @DisplayName("지원 및 합격 개수를 정상적으로 카운트한다. 임시저장 지원서는 통계에 포함하지 않는다.")
    void getStatistic() {
        // given
        int start = 1;
        int countPerStatus = 5;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, AI, TEMPORAL);
        start+=countPerStatus;
        List<Application> save = createApplicationList(start, start+countPerStatus, AI, SAVED);
        start+=countPerStatus;
        List<Application> approve = createApplicationList(start, start+countPerStatus, AI, APPROVED);
        start+=countPerStatus;
        List<Application> reject = createApplicationList(start, start+countPerStatus, AI, REJECTED);
        List<Application> allApplications = Stream.of(temporal, save, approve, reject)
                .flatMap(List::stream)
                .toList();
        int classYearStart = 1;
        List<ClassYear> allClassYears = createClassYearList(classYearStart, classYearStart+countPerStatus);
        classYearRepository.saveAll(allClassYears);
        setClassYear(allApplications, allClassYears);
        applicationRepository.saveAll(allApplications);

        // when
        ApplicationStatisticType statistic = applicationRepository.getStatistics();

        // then
        assertThat(statistic.getTotal()).isEqualTo(15);
        assertThat(statistic.getApprovedCount()).isEqualTo(5);
        assertThat(statistic.getRejectedCount()).isEqualTo(5);
        assertThat(statistic.getOpenCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("트랙별 지원 현황을 조회한다. 임시저장 지원서는 결과에 포함하지 않는다.")
    void getGroupByTrack() {
        // given
        int start = 1;
        int countPerStatus = 2;
        List<Application> ai = createApplicationList(start, start+countPerStatus, AI, SAVED);
        start+=countPerStatus;
        List<Application> backend = createApplicationList(start, start+countPerStatus, BACK_END, SAVED);
        start+=countPerStatus;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, BACK_END, TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend)
                .flatMap(List::stream)
                .toList();
        int classYearStart = 1;
        List<ClassYear> allClassYears = createClassYearList(classYearStart, classYearStart+countPerStatus);
        classYearRepository.saveAll(allClassYears);
        setClassYear(allApplications, allClassYears);
        applicationRepository.saveAll(allApplications);

        // when
        List<ApplicationTrackType> groupStatistic = applicationRepository.getGroupByTrack();
        Map<String, Integer> trackCountMap = groupStatistic.stream()
                .collect(Collectors.toMap(ApplicationTrackType::getTrack, ApplicationTrackType::getCount));

        // then
        assertThat(groupStatistic.size()).isEqualTo(2);
        assertThat(trackCountMap.get("BACK_END")).isEqualTo(2);
        assertThat(trackCountMap.get("AI")).isEqualTo(2);
    }

    @Test
    @DisplayName("트랙 / 열람여부로 지원서를 페이징 조회한다. 임시저장 지원서는 결과에 포함하지 않는다.")
    void findAllApplicationsByOption() {
        int start = 1;
        int countPerStatus = 2;
        List<Application> ai = createApplicationList(start, start+countPerStatus, AI, SAVED);
        start+=countPerStatus;
        List<Application> backend = createApplicationList(start, start+countPerStatus, BACK_END, SAVED);
        start+=countPerStatus;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, BACK_END, TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend)
                .flatMap(List::stream)
                .toList();
        int classYearStart = 1;
        List<ClassYear> allClassYears = createClassYearList(classYearStart, classYearStart+countPerStatus);
        classYearRepository.saveAll(allClassYears);
        setClassYear(allApplications, allClassYears);
        applicationRepository.saveAll(allApplications);

        // when
        PageRequest pageRequest = PageRequest.of(0,3);
        Page<Application> applicationPage = applicationRepository.findAllApplicationsByOption(pageRequest, BACK_END, false, null);

        // then
        assertThat(applicationPage).hasSize(2).allSatisfy(application -> {
            assertThat(application.getTrack()).isEqualTo(BACK_END);
            assertThat(application.getApplicationStatus()).isEqualTo(SAVED);
        });
        assertThat(applicationPage.hasNext()).isFalse();
    }


    @Test
    @DisplayName("트랙 / 열람여부에 값이 없으면 전체를 조회한다.")
    void findAllApplicationsByOptionWithNull() {
        int start = 1;
        int countPerStatus = 2;
        List<Application> ai = createApplicationList(start, start+countPerStatus, AI, SAVED);
        start+=countPerStatus;
        List<Application> backend = createApplicationList(start, start+countPerStatus, BACK_END, SAVED);
        start+=countPerStatus;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, BACK_END, TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend)
                .flatMap(List::stream)
                .toList();
        int classYearStart = 1;
        List<ClassYear> allClassYears = createClassYearList(classYearStart, classYearStart+countPerStatus);
        classYearRepository.saveAll(allClassYears);
        setClassYear(allApplications, allClassYears);
        applicationRepository.saveAll(allApplications);

        // when
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<Application> applicationPage = applicationRepository.findAllApplicationsByOption(pageRequest,null, null, null);

        // then
        assertThat(applicationPage).hasSize(4).anySatisfy(
                application -> assertThat(application.getTrack()).isEqualTo(AI)
        );
    }
}
