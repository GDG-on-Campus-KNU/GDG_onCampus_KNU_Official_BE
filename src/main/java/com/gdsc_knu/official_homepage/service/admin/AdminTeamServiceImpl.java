package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.*;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTeamServiceImpl implements AdminTeamService {
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final MemberRepository memberRepository;

    /**
     * 전체 팀의 정보를 가져옴
     * @return List<AdminTeamResponse> 전체 팀 정보
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminTeamResponse.Team> getTeamInfos() {
        return teamRepository.findAll().stream()
                .filter(team -> team.getParent() == null)
                .map(team -> AdminTeamResponse.Team.builder()
                        .id(team.getId())
                        .teamName(team.getTeamName())
                        .teamPageUrl(team.getTeamPageUrl())
                        .subTeams(team.getSubTeams().stream()
                                .map(TeamInfoResponse::new)
                                .toList())
                        .build())
                .toList();
    }

    /**
     * 새로운 부모 팀을 생성함. 직렬을 지정 하면 해당 직렬의 회원만 해당 팀에 소속
     * @param createRequest 새로운 부모 팀 생성 요청 (팀 이름, 트랙)
     * @return Long 새로 생성된 팀의 id
     */
    @Override
    @Transactional
    public Long createParentTeam(AdminTeamRequest.Create createRequest) {
        String teamName = createRequest.getTeamName();
        Track track = createRequest.getTrack();

        Team newTeam = teamRepository.save(Team.builder()
                .teamName(teamName)
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

    /**
     * 새로운 서브 팀을 생성함
     * @param parentTeamId 부모 팀의 id
     * @return Long 새로 생성된 서브 팀의 id
     */
    @Override
    @Transactional
    public Long createSubTeam(Long parentTeamId) {
        Team parentTeam = teamRepository.findById(parentTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "요청한 부모 팀이 존재하지 않습니다."));

        Team newSubTeam = teamRepository.save(Team.builder()
                .teamName(parentTeam.getTeamName() + " " + (parentTeam.getSubTeams().size() + 1) + "팀")
                .teamPageUrl(createTeamPageUrl(
                        parentTeam.getTeamName() + "-" + (parentTeam.getSubTeams().size() + 1 ) + "팀"))
                .build());
        parentTeam.addSubTeam(newSubTeam);

        return newSubTeam.getId();
    }

    /**
     * 팀의 멤버 정보를 가져옴
     * @param teamId 팀의 id
     * @return List<AdminTeamResponse.TeamMember> 팀의 멤버 정보 (id, 이름, 학번, 프로필 이미지 url)
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminTeamResponse.TeamMember> getTeamMembers(Long teamId) {
        return memberTeamRepository.findAllByTeamId(teamId);
    }

    /**
     * 멤버의 소속 팀을 변경함
     *
     * @param updateRequest 멤버 소속 팀 변경 요청 (기존 팀 id, 새로운 팀 id, 멤버 id)
     * @return Long 변경된 멤버_팀 중간 테이블 id
     */
    @Override
    @Transactional
    public Long changeTeamMember(AdminTeamRequest.Update updateRequest) {
        Long oldTeamId = updateRequest.getOldTeamId();
        Long newTeamId = updateRequest.getNewTeamId();
        Long memberId = updateRequest.getMemberId();

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

    /**
     * 부모 팀을 삭제함, 부모 팀에 속한 서브 팀도 함께 삭제됨
     * @param parentTeamId 부모 팀 id
     */
    @Override
    public void deleteParentTeam(Long parentTeamId) {
        memberTeamRepository.deleteById(parentTeamId);
    }

    /**
     * 서브 팀을 삭제함
     * @param subTeamId 서브 팀 id
     */
    @Override
    public void deleteSubTeam(Long subTeamId) {
        memberTeamRepository.deleteById(subTeamId);
    }

    /**
     * 팀 이름을 기반으로 팀 페이지 url 생성(현재는 임시 url)
     * @param teamName 팀 이름
     * @return String 팀 페이지 url
     */
    // 임시 url 생성
    private String createTeamPageUrl(String teamName) {
        return "www.gdsc-knu.com/" + teamName;
    }
}
