package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final PlatformTransactionManager transactionManager;

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<TeamInfoResponse> teamInfos = member.getMemberTeams().stream()
                .map(memberTeam -> {
                    Team team = memberTeam.getTeam();
                    return new TeamInfoResponse(team.getId(), team.getTeamName(), team.getTeamPageUrl());
                })
                .collect(Collectors.toList());

        return new MemberResponse(member,teamInfos);
    }

    @Override
    public void updateMemberInfo(Long id , MemberRequest.Update request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        String imageUrl = request.getProfileUrl() != null ?
                s3Service.upload(request.getProfileUrl()) : member.getProfileUrl();

        transactionTemplate.executeWithoutResult(status ->
            member.update(request.getName(),
                          imageUrl,
                          request.getAge(),
                          request.getMajor(),
                          request.getStudentNumber(),
                          request.getPhoneNumber(),
                          request.getIntroduction())
        );
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
    }
}
