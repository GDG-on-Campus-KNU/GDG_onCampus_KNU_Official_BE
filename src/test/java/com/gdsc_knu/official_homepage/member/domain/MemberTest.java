package com.gdsc_knu.official_homepage.member.domain;

import com.gdsc_knu.official_homepage.dto.member.MemberRequest;
import com.gdsc_knu.official_homepage.dto.oauth.GoogleUserInfo;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gdsc_knu.official_homepage.member.MemberTestEntityFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MemberTest {

    @Test
    @DisplayName("최초로 가입한 유저는 Temp 권한을 가지고, 트랙은 UNDEFINED 가 된다.")
    void createFirstMember() {
        // given
        GoogleUserInfo userInfo = new GoogleUserInfo("",
                "test.user@email.com", false,
                "테스트 유저", "","",
                "http://test.png","","");
        // when
        Member member = userInfo.toMember();

        // then
        assertThat(member.getEmail()).isEqualTo("test.user@email.com");
        assertThat(member.getName()).isEqualTo("테스트 유저");
        assertThat(member.getRole()).isEqualTo(Role.ROLE_TEMP);
        assertThat(member.getTrack()).isEqualTo(Track.UNDEFINED);
    }


    // TODO : 서비스 - 도메인 간 값 전달을 위한 DTO 계층 추가 고려
    @Test
    @DisplayName("최초 가입 정보 입력 후 GUEST 권한을 획득할 수 있다")
    void addNewMemberInfo() {
        // given
        Member member = createMember(1L);
        MemberRequest.Append request = new MemberRequest.Append("새로운 이름",24,"2000123456", "컴퓨터학부","010-1111-1111");

        // when
        member.addInfo(request.getName(), request.getAge(), request.getMajor(), request.getStudentNumber(), request.getPhoneNumber());

        // then
        assertThat(member.getName()).isEqualTo("새로운 이름");
        assertThat(member.getAge()).isEqualTo(24);
        assertThat(member.getStudentNumber()).isEqualTo("2000123456");
        assertThat(member.getMajor()).isEqualTo("컴퓨터학부");
        assertThat(member.getRole()).isEqualTo(Role.ROLE_GUEST);
    }
}
