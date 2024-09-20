package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.entity.Member;
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
