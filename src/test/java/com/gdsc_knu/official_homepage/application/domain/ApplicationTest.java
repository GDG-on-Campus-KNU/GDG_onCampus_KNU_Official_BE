package com.gdsc_knu.official_homepage.application.domain;

import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gdsc_knu.official_homepage.application.ApplicationTestEntityFactory.*;
import static com.gdsc_knu.official_homepage.member.MemberTestEntityFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
    @Test
    @DisplayName("지원서를 열람한 경우 열람상태가 변경된다.")
    void getApplicationDetail() {
        // given
        Application application = createApplication(1L, Track.AI, ApplicationStatus.SAVED);
        // when
        application.open();
        // then
        assertThat(application.isOpened()).isTrue();
    }


    @Test
    @DisplayName("임시저장 상태인 경우 status 를 변경할 수 없다.(합/불합으로 변경할 수 없다)")
    void failedUpdateTemporal() {
        // given
        Application application = createApplication(1L, Track.AI, ApplicationStatus.TEMPORAL);
        // when
        application.updateStatus(ApplicationStatus.APPROVED);
        // then
        assertThat(application.getApplicationStatus()).isEqualTo(ApplicationStatus.TEMPORAL);
    }

    @Test
    @DisplayName("지원서 엔티티 생성 시 applicationAnswer와 연관관계 매핑이 된다.")
    void createApplicationAndAnswerRelationship() {
        // given
        int answerCount = 2;
        Member member = createMember(1L);
        List<ApplicationAnswerDTO> answerList = createApplicationAnswerDTOList(answerCount, "답변");
        // when
        Application application = new Application(member, createApplicationModel(answerList));
        // then
        assertThat(application.getAnswers()).hasSize(2);
        for (int i = 0; i < answerCount; i++) {
            assertThat(application.getAnswers().get(i).getApplication()).isEqualTo(application);
        }
    }
}
