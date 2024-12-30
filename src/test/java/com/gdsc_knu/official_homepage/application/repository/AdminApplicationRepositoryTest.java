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
import static com.gdsc_knu.official_homepage.entity.enumeration.Track.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


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
        // given (status 마다 더미데이터를 countPerStatus 씩 생성한다.)
        int start = 1;
        int countPerStatus = 5;
        List<Application> temporal = createApplicationList(start, start+=countPerStatus, AI, TEMPORAL);
        List<Application> save = createApplicationList(start, start+=countPerStatus, AI, SAVED);
        List<Application> approve = createApplicationList(start, start+=countPerStatus, AI, APPROVED);
        List<Application> reject = createApplicationList(start, start+=countPerStatus, AI, REJECTED);
        List<Application> allApplications = Stream.of(temporal, save, approve, reject)
                .flatMap(List::stream)
                .toList();
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        setClassYear(allApplications, classYear);
        applicationRepository.saveAll(allApplications);

        // when
        ApplicationStatisticType statistic = applicationRepository.getStatistics(null);

        // then
        assertThat(statistic.getTotal()).isEqualTo(15);
        assertThat(statistic.getApprovedCount()).isEqualTo(5);
        assertThat(statistic.getRejectedCount()).isEqualTo(5);
        assertThat(statistic.getOpenCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("지원 및 합격 개수를 기수로 필터링하여 카운트한다.")
    void getStatisticByClassYear() {
        // given (기수 3개 지원서 9개를 생성한다.)
        List<ClassYear> classYears = createClassYearList(1, 1+3);
        classYearRepository.saveAll(classYears);
        List<Application> applications = createApplicationList(1, 1+9, AI, SAVED);
        setClassYear(applications, classYears);
        applicationRepository.saveAll(applications);

        // when
        ApplicationStatisticType statistic = applicationRepository.getStatistics(2L);

        // then (2기 지원서가 3개 조회된다.)
        assertThat(statistic.getTotal()).isEqualTo(3);
    }

    @Test
    @DisplayName("트랙별 지원 현황을 조회한다. 임시저장 지원서는 결과에 포함하지 않는다.")
    void getGroupByTrack() {
        // given (track 마다 더미데이터를 countPerStatus 씩 생성한다.)
        int start = 1;
        int countPerStatus = 2;
        List<Application> ai = createApplicationList(start, start+=countPerStatus, AI, SAVED);
        List<Application> backend = createApplicationList(start, start+=countPerStatus, BACK_END, SAVED);
        List<Application> temporal = createApplicationList(start, start+=countPerStatus, BACK_END, TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend)
                .flatMap(List::stream)
                .toList();
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        setClassYear(allApplications, classYear);
        applicationRepository.saveAll(allApplications);

        // when
        List<ApplicationTrackType> groupStatistic = applicationRepository.getGroupByTrack(null);
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
        // given (옵션 마다 더미데이터를 countPerStatus 씩 생성한다.)
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
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        setClassYear(allApplications, classYear);
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
    @DisplayName("트랙 / 열람여부 / 기수에 값이 없으면 전체를 조회한다.")
    void findAllApplicationsByOptionWithNull() {
        // given (옵션별 더미데이터를 countPerStatus 씩 생성한다.)
        int start = 1;
        int countPerStatus = 2;
        List<Application> ai = createApplicationList(start, start+=countPerStatus, AI, SAVED);
        List<Application> backend = createApplicationList(start, start+=countPerStatus, BACK_END, SAVED);
        List<Application> temporal = createApplicationList(start, start+=countPerStatus, BACK_END, TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend)
                .flatMap(List::stream)
                .toList();
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        setClassYear(allApplications, classYear);
        applicationRepository.saveAll(allApplications);

        // when
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<Application> applicationPage = applicationRepository.findAllApplicationsByOption(pageRequest,null, null, null);

        // then
        assertThat(applicationPage).hasSize(4).anySatisfy(
                application -> assertThat(application.getTrack()).isEqualTo(AI)
        );
    }

    @Test
    @DisplayName("기수별 지원서를 페이징 조회한다.")
    void findAllByClassYear() {
        // given (총 6개의 지원서와 3개의 기수를 생성하여 매핑한다.)
        List<ClassYear> classYears = createClassYearList(1, 1+3);
        classYearRepository.saveAll(classYears);
        List<Application> applications = createApplicationList(1, 1+6, AI, SAVED);
        setClassYear(applications, classYears);
        applicationRepository.saveAll(applications);

        // when
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<Application> applicationPage = applicationRepository.findAllApplicationsByOption(pageRequest,null, null, 1L);

        // then
        // 필터링 된 지원서 검증
        assertThat(applicationPage).hasSize(2).allSatisfy(
                application -> assertThat(application.getClassYear().getId()).isEqualTo(1L)
        );
        // count 쿼리 검증
        assertAll(()->
            {
                assertThat(applicationPage.getTotalPages()).isEqualTo(1L);
                assertThat(applicationPage.hasNext()).isFalse();
            }
        );
    }
}
