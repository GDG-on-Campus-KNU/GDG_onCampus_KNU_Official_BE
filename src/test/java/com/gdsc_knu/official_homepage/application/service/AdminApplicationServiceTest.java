package com.gdsc_knu.official_homepage.application.service;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.createApplicationList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;


@SpringBootTest
@Transactional
public class AdminApplicationServiceTest {
    @Autowired private AdminApplicationService applicationService;
    @Autowired private ApplicationRepository applicationRepository;
    @MockBean private MailService mailService;


    @AfterEach
    void clear() {
        applicationRepository.deleteAll();
    }

    // TODO: 스프링부트 테스트 없이 단위테스트
    @Test
    @DisplayName("지원서를 열람한 경우 열람상태가 변경된다.")
    void getApplicationDetail() {
        // given
        Application application = createApplication(ApplicationStatus.SAVED);
        applicationRepository.save(application);
        // when
        applicationService.getApplicationDetail(application.getId());
        // then
        assertThat(application.isOpened()).isTrue();
    }


    // TODO: 스프링부트 테스트 없이 단위테스트
    @Test
    @DisplayName("지원서 목록 요청에 따라 필요한 데이터만 조회된다.")
    void getApplicationsByOption() {

    }

    @Test
    @DisplayName("메일 전송에 실패하더라도 status 변경은 저장한다.(SAVED -> APPROVED")
    void updateStatus() {
        // given
        Application application = createApplication(ApplicationStatus.SAVED);
        applicationRepository.save(application);
        doThrow(CustomException.class).when(mailService).sendEach(application);
        // when
        // then
        assertThrows(CustomException.class, () ->
                applicationService.decideApplication(application.getId(), ApplicationStatus.APPROVED)
        );
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.APPROVED);
    }

    // dao 의존성 없앤 단위테스트로 변경
    @Test
    @DisplayName("임시저장 상태인 경우 status 를 변경할 수 없다.")
    void failedUpdateTemporal() {
        // given
        Application application = createApplication(ApplicationStatus.TEMPORAL);
        applicationRepository.save(application);
        // when
        applicationService.decideApplication(application.getId(), ApplicationStatus.APPROVED);
        // then
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.TEMPORAL);
    }


    // TODO: 트랙별 지원현황을 읽어오는 테스트와, 읽어온 데이터를 가공하는 테스트 분리
    @Test
    @DisplayName("트랙별 지원서의 개수를 정상적으로 카운트한다.")
    void getTrackStatistic() {
        // given
        int start = 1;
        int countPerStatus = 2;
        ApplicationStatus status = ApplicationStatus.SAVED;
        List<Application> ai = createApplicationList(start, start+countPerStatus, Track.AI, status);
        start+=countPerStatus;
        List<Application> backend = createApplicationList(start, start+countPerStatus, Track.BACK_END, status);
        start+=countPerStatus;
        List<Application> frontend = createApplicationList(start, start+countPerStatus, Track.FRONT_END, status);
        start+=countPerStatus;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, Track.BACK_END, ApplicationStatus.TEMPORAL);
        List<Application> allApplications = Stream.of(temporal, ai, backend, frontend)
                .flatMap(List::stream)
                .toList();
        applicationRepository.saveAll(allApplications);
        // when
        Map<String, Integer> statistic = applicationService.getTrackStatistic();
        // then
        assertThat(statistic.get("BACK_END")).isEqualTo(2);
        assertThat(statistic.get("FRONT_END")).isEqualTo(2);
        assertThat(statistic.get("AI")).isEqualTo(2);
        assertThat(statistic.get("DESIGNER")).isEqualTo(0);
        assertThat(statistic.get("TOTAL")).isEqualTo(6);
        assertThat(statistic.size()).isEqualTo(6);
    }



    private Application createApplication(ApplicationStatus status) {
        return Application.builder()
                .studentNumber("2024000000")
                .phoneNumber("010-0000-0000")
                .email("test@email.com")
                .applicationStatus(status)
                .track(Track.BACK_END)
                .build();
    }

}
