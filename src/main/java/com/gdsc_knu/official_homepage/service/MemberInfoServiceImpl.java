package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.MemberInfoRequest;
import com.gdsc_knu.official_homepage.dto.MemberInfoResponse;
import com.gdsc_knu.official_homepage.dto.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberRepository memberRepository;

    @Override
    public MemberInfoResponse retrieveMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<TeamInfoResponse> teamInfos = new ArrayList<>();

        for (MemberTeam memberTeam: member.getMemberTeams()){
            Team team = memberTeam.getTeam();
            teamInfos.add(new TeamInfoResponse(team.getTeamName(),team.getTeamPageUrl()));
        }

        return new MemberInfoResponse(member,teamInfos);
    }


    @Transactional
    @Override
    public Long updateMemberInfo(Long id, MemberInfoRequest memberInfoRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        member.update(memberInfoRequest);
        memberRepository.save(member);

        return member.getId();
    }
}
