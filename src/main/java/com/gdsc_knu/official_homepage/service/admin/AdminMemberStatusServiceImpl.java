package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.*;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberStatusServiceImpl implements AdminMemberStatusService {
    private final MemberRepository memberRepository;

    /**
     * 전체 회원 정보를 가져옴
     * @param page 페이지 번호(기본 값 0)
     * @param size 페이지 크기(기본 값 10)
     * @return PagingResponse<AdminTeamResponse.TeamMember> 전체 회원 정보
     */
    @Override
    @Transactional(readOnly = true)
    public PagingResponse<AdminMemberResponse> getAllMemberInfos(int page, int size) {
        Page<Member> memberPage = memberRepository.findAll(PageRequest.of(page, size));
        return PagingResponse.from(memberPage, AdminMemberResponse::from);
    }

    /**
     * 회원 정보를 일괄 삭제함
     * @param deleteRequest (삭제할 회원 id 리스트)
     */
    @Override
    @Transactional
    public void deleteMember(AdminMemberRequest.Delete deleteRequest) {
        memberRepository.deleteAllById(deleteRequest.getUserIds());
    }

    /**
    * 회원들의 권한을 일괄 변경함
    * @param roleUpdateRequest (변경할 회원 id 리스트, 변경할 권한)
    * @return Long 변경된 회원 수
    */
    @Override
    @Transactional
    public Long updateMemberRole(AdminMemberRequest.RoleUpdate roleUpdateRequest) {
        List<Long> userIds = roleUpdateRequest.getUserIds();
        Role newRole = roleUpdateRequest.getRole();

        return userIds.stream()
                .map(userId -> {
                    try {
                        Member member = memberRepository.findById(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
                        member.updateRole(newRole);
                        return 1L;
                    } catch (CustomException e) {
                        log.warn("존재하지 않는 userId로 역할 갱신 시도 오류: {}", userId);
                        return 0L;
                    }
                })
                .reduce(0L, Long::sum);
    }

    /**
     * 회원들의 직렬을 일괄 변경함
     * @param trackUpdateRequest (변경할 회원 id 리스트, 변경할 직렬)
     * @return Long 변경된 회원 수
     */
    @Override
    @Transactional
    public Long updateMemberTrack(AdminMemberRequest.TrackUpdate trackUpdateRequest) {
        List<Long> userIds = trackUpdateRequest.getUserIds();
        Track newTrack = trackUpdateRequest.getTrack();

        return userIds.stream()
                .map(userId -> {
                    try {
                        Member member = memberRepository.findById(userId)
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
                        member.updateTrack(newTrack);
                        return 1L;
                    } catch (CustomException e) {
                        log.warn("존재하지 않는 userId로 직렬 갱신 시도 오류: {}", userId);
                        return 0L;
                    }
                })
                .reduce(0L, Long::sum);
    }

    /**
     * 이름 검색으로 회원 정보를 가져옴
     * @param name 검색할 이름
     * @param page 페이지 번호(기본 값 0)
     * @param size 페이지 크기(기본 값 10)
     * @return PagingResponse<AdminTeamResponse.TeamMember> 검색된 회원 정보
     */
    @Override
    @Transactional(readOnly = true)
    public PagingResponse<AdminMemberResponse> getMemberByName(String name, int page, int size) {
        Page<Member> memberPage
                = memberRepository.findByNameContaining(PageRequest.of(page, size), name);
        return PagingResponse.from(memberPage, AdminMemberResponse::from);
    }


}
