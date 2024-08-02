package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.repository.MemberTeamRepository;
import com.gdsc_knu.official_homepage.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTeamServiceImpl implements AdminTeamService {
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminTeamInfoResponse> getTeamInfos() {
        return teamRepository.findAll().stream()
                .map(team -> AdminTeamInfoResponse.builder()
                        .id(team.getId())
                        .teamName(team.getTeamName())
                        .teamPageUrl(team.getTeamPageUrl()).build())
                .toList();
    }

    @Override
    @Transactional
    public Long createTeam(String teamName, Track track) {
        Team newTeam = teamRepository.save(Team.builder()
                .teamName(teamName)
                .teamPageUrl(createTeamPageUrl(teamName))
                .build());
        // 첫 생성 시 모든 인원 0팀으로 설정
        Team initialSubTeam = Team.builder()
                .teamName("0팀")
                .build();
        newTeam.addSubTeam(initialSubTeam);
        List<Member> members;
        if (track != null) { // 직렬별로 구분하는 스터디 팀이라면
            members = memberRepository.findAllByTrack(track);
        }
        else {
            members = memberRepository.findAll();
        }
        List<MemberTeam> memberTeams = new ArrayList<>();
        for (Member member : members) {
            memberTeams.add(MemberTeam.builder()
                    .member(member)
                    .team(newTeam)
                    .build());
        }
        memberTeamRepository.saveAll(memberTeams);

        return newTeam.getId();
    }

    @Override
    @Transactional
    public Long createSubTeam(Long parentTeamId) {
        Team parentTeam = teamRepository.findById(parentTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "요청한 상위 팀이 존재하지 않습니다."));
        if (parentTeam.getSubTeams().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "상위 팀의 구성이 잘못 되었습니다.");
        }
        Team newSubTeam = Team.builder()
                .teamName(parentTeam.getTeamName() + " " + (parentTeam.getSubTeams().size() - 1) + "팀")
                .teamPageUrl(createTeamPageUrl(
                        parentTeam.getTeamName() + "_" + (parentTeam.getSubTeams().size() - 1) + "팀"))
                .build();
        parentTeam.addSubTeam(newSubTeam);

        return newSubTeam.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminMemberResponse> getTeamMembers(Long teamId) {
        return memberTeamRepository.findAllAdminMemberResponsesByTeamId(teamId);
    }

    @Override
    @Transactional
    public Long changeTeamMember(Long teamId, Long memberId) {
        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "잘못된 팀 변경 요청입니다."));
        Team newTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "옮길 팀이 존재하지 않습니다."));
        memberTeam.changeTeam(newTeam);

        return memberTeamRepository.save(memberTeam).getId();
    }

    // 임시 url 생성
    private String createTeamPageUrl(String teamName) {
        return "www.gdsc-knu.com/" + teamName;
    }
}
