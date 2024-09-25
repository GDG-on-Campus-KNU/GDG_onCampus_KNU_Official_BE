package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.team.*;
import com.gdsc_knu.official_homepage.dto.member.TeamInfoResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.MemberTeam;
import com.gdsc_knu.official_homepage.entity.Team;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
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
import java.util.Objects;

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
                        .subTeams(team.getSubTeams().stream()
                                .map(TeamInfoResponse::new)
                                .toList())
                        .build())
                .toList();
    }

    /**
     * 새로운 부모 팀을 생성함. 직렬을 지정 하면 해당 직렬의 회원(MEMBER,CORE)만 해당 팀에 소속
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
                .build());

        List<Member> members = (track != null)
                ? memberRepository.findAllByTrack(track)
                : memberRepository.findAll();
        members.removeIf(member -> member.getRole().equals(Role.ROLE_GUEST) || member.getRole().equals(Role.ROLE_TEMP));

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
        long oldTeamId = updateRequest.getOldTeamId();
        long newTeamId = updateRequest.getNewTeamId();
        long memberId = updateRequest.getMemberId();

        if (oldTeamId == newTeamId) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "동일한 팀으로 변경할 수 없습니다.");
        }

        MemberTeam memberTeam = memberTeamRepository.findByMemberIdAndTeamId(memberId, oldTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "잘못된 팀 변경 요청입니다."));
        Team oldTeam = teamRepository.findById(oldTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "기존 팀이 존재하지 않습니다."));
        Team newTeam = teamRepository.findById(newTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "옮길 팀이 존재하지 않습니다."));
        long newTeamParentId = newTeam.getParent() == null ? -1 : newTeam.getParent().getId();
        if (oldTeam.getParent() == null) { // 기존 팀이 부모 팀인 경우
            if (newTeamParentId == -1 || oldTeamId != newTeamParentId) {
                // 다른 부모 팀으로 이동, 또는 다른 부모 팀의 서브 팀으로 이동 금지
                throw new CustomException(ErrorCode.INVALID_INPUT, "다른 부모팀으로 변경할 수 없습니다.");
            }
        }
        else { // 기존 팀이 서브 팀인 경우
            long oldTeamParentId = oldTeam.getParent().getId();
            if ((newTeamParentId == -1 && oldTeamParentId != newTeamId)
                    || (newTeamParentId != -1 && oldTeamParentId != newTeamParentId)) {
                // 다른 부모 팀으로 이동, 또는 다른 부모 팀의 서브 팀으로 이동 금지
                throw new CustomException(ErrorCode.INVALID_INPUT, "다른 부모팀으로 변경할 수 없습니다.");
            }
        }

        memberTeam.changeTeam(newTeam);

        return memberTeamRepository.save(memberTeam).getId();
    }

    /**
     * 부모 팀을 삭제함, 부모 팀에 속한 서브 팀도 함께 삭제됨
     * @param parentTeamId 부모 팀 id
     */
    @Override
    @Transactional
    public void deleteParentTeam(Long parentTeamId) {
        teamRepository.deleteById(parentTeamId);
    }

    /**
     * 해당 부모 팀의 마지막 서브 팀을 삭제함
     * @param parentTeamId 삭제할 마지막 서브 팀의 부모 팀 id
     */
    @Override
    @Transactional
    public void deleteSubTeam(Long parentTeamId) {
        Team parentTeam = teamRepository.findById(parentTeamId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT, "요청한 부모 팀이 존재하지 않습니다."));
        List<Team> subTeams = parentTeam.getSubTeams();
        if (subTeams.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "삭제할 서브 팀이 존재하지 않습니다.");
        }
        long deleteSubTeamId = subTeams.get(parentTeam.getSubTeams().size() - 1).getId();
        // 삭제 될 서브 팀에 속한 멤버들을 부모 팀으로 이동
        List<MemberTeam> memberTeams = memberTeamRepository.findAllMemberTeamByTeamId(deleteSubTeamId);
        for (MemberTeam memberTeam : memberTeams) {
            memberTeam.changeTeam(parentTeam);
        }
        subTeams.remove(subTeams.size() - 1);
        teamRepository.deleteById(deleteSubTeamId);
    }
}
