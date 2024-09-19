package com.gdsc_knu.official_homepage.dto.team;

import com.gdsc_knu.official_homepage.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private Long id;
        private String name;
        private String track;

        public static MemberInfo from(Member member) {
            return MemberInfo.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .track(member.getTrack().name())
                    .build();
        }
    }
}
