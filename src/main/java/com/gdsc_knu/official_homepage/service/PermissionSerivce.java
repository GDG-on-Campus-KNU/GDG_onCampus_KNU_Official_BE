package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.dto.role.UserRoleRequest;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionSerivce {
    private final MemberRepository memberRepository;

    /**
     * GUEST에서 MEMBER로 승인
     * ROLE이 GUEST가 아닌 경우 무시
     * 정상적으로 승인된 USER의 수를 반환한다.
     */
    @Transactional
    public Long approveMember(UserRoleRequest request){
        List<Long> userIds = request.getUserIds();
        return userIds.stream()
                .map(userId -> memberRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)))
                .filter(member -> member.getRole() == Role.ROLE_GUEST)
                .peek(member -> member.updateRole(Role.ROLE_MEMBER))
                .count();
    }
}
