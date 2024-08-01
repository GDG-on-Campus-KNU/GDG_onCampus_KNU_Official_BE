package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberDeleteRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberInfoResponse;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberRoleRequest;
import com.gdsc_knu.official_homepage.dto.admin.memberStatus.MemberTrackRequest;
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
    @Override
    @Transactional(readOnly = true)
    public PagingResponse<MemberInfoResponse> getAllMemberInfos(int page, int size) {
        Page<Member> memberPage = memberRepository.findAll(PageRequest.of(page, size));
        return PagingResponse.from(memberPage, MemberInfoResponse::from);
    }

    @Override
    @Transactional
    public void deleteMember(MemberDeleteRequest memberDeleteRequest) {
        memberRepository.deleteAllById(memberDeleteRequest.getUserIds());
    }

    @Override
    @Transactional
    public Long updateMemberRole(MemberRoleRequest memberRoleRequest) {
        List<Long> userIds = memberRoleRequest.getUserIds();
        Role newRole = memberRoleRequest.getRole();

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

    @Override
    @Transactional
    public Long updateMemberTrack(MemberTrackRequest memberTrackRequest) {
        List<Long> userIds = memberTrackRequest.getUserIds();
        Track newTrack = memberTrackRequest.getTrack();

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
}
