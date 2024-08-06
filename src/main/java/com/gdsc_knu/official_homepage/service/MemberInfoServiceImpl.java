package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.service.fileupload.S3FileUploader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberRepository memberRepository;
    private final S3FileUploader fileUploader;

    @Override
    public MemberResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<TeamInfoResponse> teamInfos = member.getMemberTeams().stream()
                .map(memberTeam -> {
                    Team team = memberTeam.getTeam();
                    return new TeamInfoResponse(team.getTeamName(), team.getTeamPageUrl());
                })
                .collect(Collectors.toList());

        return new MemberResponse(member,teamInfos);
    }

    @Transactional
    @Override
    public void updateMemberInfo(Long id , MemberRequest.Update request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String imageUrl = request.getProfileUrl() != null ?
                fileUploader.upload(request.getProfileUrl()) : member.getProfileUrl();
        member.update(request.getName(),
                imageUrl,
                request.getAge(),
                request.getMajor(),
                request.getStudentNumber(),
                request.getPhoneNumber(),
                request.getIntroduction());
        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void addMemberInfo(Long id, MemberRequest.Append request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        member.addInfo(request.getName(),
                request.getAge(),
                request.getMajor(),
                request.getStudentNumber(),
                request.getPhoneNumber());
        memberRepository.save(member);
    }
}
