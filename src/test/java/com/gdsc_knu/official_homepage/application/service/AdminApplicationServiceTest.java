package com.gdsc_knu.official_homepage.application.service;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import jakarta.persistence.EntityManager;
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

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.createApplication;
import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.createApplicationList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;


@SpringBootTest
@Transactional
public class AdminApplicationServiceTest {
    @Autowired private AdminApplicationService applicationService;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private EntityManager em;
    @MockBean private MailService mailService;


    @AfterEach
    void clear() {
        applicationRepository.deleteAll();
    }


    @Test
    @DisplayName("메일 전송에 실패하더라도 status 변경은 저장한다.(SAVED -> APPROVED")
    void updateStatus() {
        // given
        Application application = createApplication(0, Track.AI, ApplicationStatus.SAVED);
        applicationRepository.save(application);
        doThrow(CustomException.class).when(mailService).sendEach(application);
        // when
        // then
        assertThrows(CustomException.class, () ->
                applicationService.decideApplication(application.getId(), ApplicationStatus.APPROVED)
        );
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.APPROVED);
    }


    @Test
    @DisplayName("트랙별 지원서의 개수를 정상적으로 카운트한다. (트랙에 지원이 없는 경우 0을 카운트한다.)")
    void getTrackStatistic() {
        // given
        int start = 2;
        int countPerStatus = 2;
        ApplicationStatus status = ApplicationStatus.SAVED;
        List<Application> ai = createApplicationList(start, start+countPerStatus, Track.AI, status);
        start+=countPerStatus;
        List<Application> backend = createApplicationList(start, start+countPerStatus, Track.BACK_END, status);
        start+=countPerStatus;
        List<Application> frontend = createApplicationList(start, start+countPerStatus, Track.FRONT_END, status);
        start+=countPerStatus;
        List<Application> temporal = createApplicationList(start, start+countPerStatus, Track.BACK_END, ApplicationStatus.TEMPORAL);
        List<Application> allApplications = Stream.of(ai, backend, frontend, temporal)
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
        assertThat(statistic.get("ANDROID")).isEqualTo(0);
        assertThat(statistic.get("TOTAL")).isEqualTo(6);
        assertThat(statistic.size()).isEqualTo(6);
    }

}
