package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Comparator;
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
                .filter(memberTeam -> memberTeam.getTeam().getParent() != null)
                .map(memberTeam -> new TeamInfoResponse(memberTeam.getTeam()))
                .collect(Collectors.toList());

        return new MemberResponse(member,teamInfos);
    }

    @Override
    public void updateMemberInfo(Long id , MemberRequest.Update request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String imageUrl = request.getProfileUrl() != null
                ? s3Service.upload(request.getProfileUrl(), member.getEmail().split("@")[0])
                : member.getProfileUrl();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
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

    /**
     * 중간테이블이라 인덱스도 다 있고
     * join하는 데이터가 많지 않아서 SQL쿼리로 한번에 가져오는 것도 좋을 것 같아요
     */
    @Override
    @Transactional(readOnly = true)
    public List<TeamInfoResponse> getMemberTeamInfo(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND))
                .getMemberTeams().stream()
                .filter(memberTeam -> memberTeam.getTeam().getParent() != null)
                .sorted(Comparator.comparing(MemberTeam::getId).reversed())
                .map(memberTeam -> new TeamInfoResponse(memberTeam.getTeam()))
                .collect(Collectors.toList());
    }
}
