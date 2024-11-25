package com.gdsc_knu.official_homepage.application.domain;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.createApplication;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
    @Test
    @DisplayName("지원서를 열람한 경우 열람상태가 변경된다.")
    void getApplicationDetail() {
        // given
        Application application = createApplication(1, Track.AI, ApplicationStatus.SAVED);
        // when
        application.open();
        // then
        assertThat(application.isOpened()).isTrue();
    }


    @Test
    @DisplayName("임시저장 상태인 경우 status 를 변경할 수 없다.(합/불합으로 변경할 수 없다)")
    void failedUpdateTemporal() {
        // given
        Application application = createApplication(1, Track.AI, ApplicationStatus.TEMPORAL);
        // when
        application.updateStatus(ApplicationStatus.APPROVED);
        // then
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.TEMPORAL);
    }
}
