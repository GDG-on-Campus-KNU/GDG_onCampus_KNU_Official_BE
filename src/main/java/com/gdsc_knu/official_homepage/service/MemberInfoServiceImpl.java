package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl implements MemberInfoService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        List<TeamInfoResponse> teams = member.getTeams().stream()
                .map(TeamInfoResponse::from)
                .toList();

        return new MemberResponse(member,teams);
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
                .getTeams().stream()
                .sorted(Comparator.comparing(Team::getId).reversed())
                .map(TeamInfoResponse::new)
                .toList();
    }
}
