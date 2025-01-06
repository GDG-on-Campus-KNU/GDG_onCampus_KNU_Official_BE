package com.gdsc_knu.official_homepage.application.service;

import com.gdsc_knu.official_homepage.ClearDatabase;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.application.ClassYearRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import com.gdsc_knu.official_homepage.service.admin.AdminApplicationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;


@SpringBootTest
@Import(ClearDatabase.class)
public class AdminApplicationServiceTest {
    @Autowired private AdminApplicationService applicationService;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private ClassYearRepository classYearRepository;
    @Autowired private ClearDatabase clearDatabase;
    @MockBean private MailService mailService;


    @AfterEach
    void tearDown() {
        clearDatabase.each("application");
        clearDatabase.each("class_year");
    }


    @Test
    @Transactional
    @DisplayName("메일 전송에 실패하더라도 status 변경은 저장한다.(SAVED -> APPROVED)")
    void updateStatus() {
        // given
        Application application = createApplication(null, Track.AI, ApplicationStatus.SAVED);
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        application.updateClassYear(classYear);
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
        int classYearStart = 1;
        List<ClassYear> allClassYears = createClassYearList(classYearStart, classYearStart+countPerStatus);
        classYearRepository.saveAll(allClassYears);
        setClassYear(allApplications, allClassYears);
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

    @Test
    @DisplayName("지원서 메모가 이미 수정된 경우 다시 수정을 시도할 때 오류를 반환한다.")
    void updateNoteFailed() {
        // given
        Application application = createApplication(null, Track.AI, ApplicationStatus.SAVED);
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        application.updateClassYear(classYear);
        applicationRepository.save(application);
        // 다른 사용자에 의해 변경
        application.saveNote("1",application.getVersion());
        applicationRepository.save(application);
        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                applicationService.noteApplication(application.getId(),"1,2",application.getVersion()-1)
        );
        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONCURRENT_FAILED);
        Application finalApplication = applicationRepository.findById(application.getId()).orElseThrow();
        assertThat(finalApplication.getNote()).isEqualTo("1"); // 2번째 변경내용은 반영되지 않는다.
    }

    @Test
    @DisplayName("동시에 지원서를 수정하는 경우 처음 시도만 남고 오류를 반환한다.")
    void updateNoteConcurrentFailed() throws InterruptedException {
        Application application = createApplication(null, Track.AI, ApplicationStatus.SAVED);
        ClassYear classYear = createClassYear(1L);
        classYearRepository.save(classYear);
        application.updateClassYear(classYear);
        applicationRepository.saveAndFlush(application);

        int threadCount = 2;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // when
        for (int i=0; i<threadCount; i++) {
            String note = String.valueOf(i);
            executor.execute(() -> {
                try {
                    applicationService.noteApplication(application.getId(), note, application.getVersion());
                } catch (CustomException e) {
                    assertThat(e.getErrorCode()).isEqualTo(ErrorCode.CONCURRENT_FAILED);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Application finalApplication = applicationRepository.findById(application.getId()).orElseThrow();
        assertThat(finalApplication.getVersion()).isEqualTo(1);
    }
}
