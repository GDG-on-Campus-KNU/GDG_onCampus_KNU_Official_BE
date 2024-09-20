package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.team.TeamResponse;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberTeamRepository;
import com.gdsc_knu.official_homepage.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;

    public List<TeamResponse.MemberInfo> getTeamMember(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀입니다."));
        if (team.getParent() == null)
            throw new CustomException(ErrorCode.INVALID_INPUT, "정상 배정된 팀이 아닙니다.");

        List<MemberTeam> memberTeams = memberTeamRepository.findMembersByTeamId(teamId);
        return memberTeams.stream()
                .map(memberTeam -> TeamResponse.MemberInfo.from(memberTeam.getMember()))
                .toList();
    }

    public List<TeamResponse.MemberInfo> getRecentTeamMember(Long memberId) {
        List<MemberTeam> memberTeams = memberTeamRepository.findByMemberOrderByTeamId(memberId);

        Team team = memberTeams.stream()
                .map(MemberTeam::getTeam)
                .filter(candidateTeam -> candidateTeam.getParent() != null)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("배정된 팀이 없는 사용자입니다."));

        List<MemberTeam> members = memberTeamRepository.findMembersByTeamId(team.getId());
        return members.stream()
                .map(member -> TeamResponse.MemberInfo.from(member.getMember()))
                .toList();
    }
}
