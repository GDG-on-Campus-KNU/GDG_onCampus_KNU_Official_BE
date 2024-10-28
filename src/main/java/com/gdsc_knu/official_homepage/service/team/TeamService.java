package com.gdsc_knu.official_homepage.service.team;

import com.gdsc_knu.official_homepage.dto.member.MemberResponse;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.team.MemberTeamRepository;
import com.gdsc_knu.official_homepage.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;

    public List<MemberResponse.WithTrack> getTeamMember(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 팀입니다."));
        if (team.getParent() == null)
            throw new CustomException(ErrorCode.INVALID_INPUT, "정상 배정된 팀이 아닙니다.");

        List<MemberTeam> memberTeams = memberTeamRepository.findMembersByTeamId(teamId);
        return memberTeams.stream()
                .map(memberTeam -> MemberResponse.WithTrack.from(memberTeam.getMember()))
                .toList();
    }
}
