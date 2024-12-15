package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.member.*;
import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse.Main getMemberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return MemberResponse.Main.from(member);
    }

    @Transactional
    public void addMemberInfo(Long id, MemberRequest.Append request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.addInfo(request.getName(),
                request.getAge(),
                request.getMajor(),
                request.getStudentNumber(),
                request.getPhoneNumber());
        try {
            memberRepository.saveAndFlush(member);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_DUPLICATED);
        }
    }

    /**
     * 중간테이블이라 인덱스도 다 있고
     * join하는 데이터가 많지 않아서 SQL쿼리로 한번에 가져오는 것도 좋을 것 같아요
     * TODO : Team의 책임인지 고려
     */
    @Transactional(readOnly = true)
    public List<TeamResponse.Main> getMemberTeamInfo(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getTeams().stream()
                .sorted(Comparator.comparing(Team::getId).reversed())
                .map(TeamResponse.Main::from)
                .toList();
    }

    /**
     * 개발자 편의 메서드 (실제 사용 X)
     */
    @Transactional
    public Member getMemberAdmin(String email, Track track, Role role) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        member.updateRole(role);
        member.updateTrack(track);
        return member;
    }
}
