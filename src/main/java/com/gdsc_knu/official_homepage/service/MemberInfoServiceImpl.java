package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.MemberInfoAdd;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoUpdate;
import com.gdsc_knu.official_homepage.dto.member.MemberInfoResponse;
import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberRepository memberRepository;

    @Override
    public MemberInfoResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<TeamInfoResponse> teamInfos = member.getMemberTeams().stream()
                .map(memberTeam -> {
                    Team team = memberTeam.getTeam();
                    return new TeamInfoResponse(team.getTeamName(), team.getTeamPageUrl());
                })
                .collect(Collectors.toList());

        return new MemberInfoResponse(member,teamInfos);
    }

    @Transactional
    @Override
    public void updateMemberInfo(Long id, MemberInfoUpdate request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        member.update(request.getName(),
                request.getProfileUrl(),
                request.getAge(),
                request.getMajor(),
                request.getStudentNumber(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getIntroduction());
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void addMemberInfo(Long id, MemberInfoAdd memberInfo) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        member.addInfo(memberInfo.getName(),
                memberInfo.getAge(),
                memberInfo.getMajor(),
                memberInfo.getStudentNumber(),
                memberInfo.getPhoneNumber(),
                memberInfo.getTrack());
        memberRepository.save(member);
    }
}
