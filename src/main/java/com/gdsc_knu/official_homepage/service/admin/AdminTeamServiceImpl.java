package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.AdminMemberResponse;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamChangeRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamCreateRequest;
import com.gdsc_knu.official_homepage.dto.admin.team.AdminTeamResponse;
import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
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
    public List<AdminTeamResponse> getTeamInfos() {
        return teamRepository.findAll().stream()
                .filter(team -> team.getParent() == null)
                .map(team -> AdminTeamResponse.builder()
                        .id(team.getId())
                        .teamName(team.getTeamName())
                        .teamPageUrl(team.getTeamPageUrl())
                        .subTeams(team.getSubTeams().stream()
                                .map(TeamInfoResponse::new)
                                .toList())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public Long createTeam(AdminTeamCreateRequest adminTeamCreateRequest) {
        String teamName = adminTeamCreateRequest.getTeamName();
        Track track = adminTeamCreateRequest.getTrack();

        Team newTeam = teamRepository.save(Team.builder()
                .teamName(teamName)
                .subTeams(new ArrayList<>())
                .teamPageUrl(createTeamPageUrl(teamName))
                .build());

        List<Member> members = (track != null)
                ? memberRepository.findAllByTrack(track)
                : memberRepository.findAll();
        List<MemberTeam> memberTeams = members.stream()
                .map(member -> MemberTeam.builder()
                        .member(member)
                        .team(newTeam)
                        .build())
                .toList();
        memberTeamRepository.saveAll(memberTeams);

        return newTeam.getId();
    }

    @Override
    @Transactional
    public Long createSubTeam(Long parentTeamId) {
        Team parentTeam = teamRepository.findById(parentTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "요청한 상위 팀이 존재하지 않습니다."));

        Team newSubTeam = teamRepository.save(Team.builder()
                .teamName(parentTeam.getTeamName() + " " + (parentTeam.getSubTeams().size() + 1) + "팀")
                .teamPageUrl(createTeamPageUrl(
                        parentTeam.getTeamName() + "-" + (parentTeam.getSubTeams().size() + 1 ) + "팀"))
                .build());
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
    public Long changeTeamMember(AdminTeamChangeRequest adminTeamChangeRequest) {
        Long oldTeamId = adminTeamChangeRequest.getOldTeamId();
        Long newTeamId = adminTeamChangeRequest.getNewTeamId();
        Long memberId = adminTeamChangeRequest.getMemberId();

        if (oldTeamId.equals(newTeamId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "동일한 팀으로 변경할 수 없습니다.");
        }

        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, oldTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "잘못된 팀 변경 요청입니다."));
        Team newTeam = teamRepository.findById(newTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "옮길 팀이 존재하지 않습니다."));
        memberTeam.changeTeam(newTeam);

        return memberTeamRepository.save(memberTeam).getId();
    }

    // 임시 url 생성
    private String createTeamPageUrl(String teamName) {
        return "www.gdsc-knu.com/" + teamName;
    }
}
