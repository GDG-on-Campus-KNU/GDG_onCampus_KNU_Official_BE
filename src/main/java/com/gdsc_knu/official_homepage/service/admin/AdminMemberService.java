package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberResponse.WithTrack> getMembersByTrack(Track track) {
        return memberRepository.findAllByTrackOrderByName(track).stream()
                .map(MemberResponse.WithTrack::from)
                .toList();
    }
}
