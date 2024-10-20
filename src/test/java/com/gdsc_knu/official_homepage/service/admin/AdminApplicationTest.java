package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;


@SpringBootTest
public class AdminApplicationTest {
    @Autowired private AdminApplicationService applicationService;
    @Autowired private ApplicationRepository applicationRepository;
    @MockBean private MailService mailService;


    @AfterEach
    void clear() {
        applicationRepository.deleteAll();
    }

    @Test
    @DisplayName("메일 전송에 실패하더라도 status 변경은 저장한다.")
    void updateStatus() {

    }

    @Test
    @DisplayName("임시저장 상태인 경우 status 를 변경할 수 없다.")
    void failedUpdateTemporal() {
        // given
        Application application = createApplication(ApplicationStatus.TEMPORAL);
        applicationRepository.save(application);
        doThrow(CustomException.class).when(mailService).sendEach(application);
        // when
        applicationService.decideApplication(application.getId(), ApplicationStatus.APPROVED);
        // then
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.TEMPORAL);
    }


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
    }





    private List<Application> createApplicationList(int startNum, int count, Track track, ApplicationStatus status){
        List<Application> applicationList = new ArrayList<>();
        for (int i=startNum; i<count; i++) {
            applicationList.add(createApplicationByTrack(i, track, status));
        }
        return applicationList;
    }

    private Application createApplicationByTrack(int id, Track track, ApplicationStatus status) {
        return Application.builder()
                .email("test"+id+"@email.com")
                .studentNumber("202400"+id)
                .phoneNumber("010-0000-"+id)
                .applicationStatus(status)
                .track(track)
                .build();
    }

    private Application createApplication(ApplicationStatus status) {
        return Application.builder()
                .studentNumber("2024000000")
                .phoneNumber("010-0000-0000")
                .email("test@email.com")
                .applicationStatus(status)
                .build();
    }

}
