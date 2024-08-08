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

    //❗삭제 예정❗
    @Transactional
    public void toCore(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        member.updateRole(Role.ROLE_CORE);
    }
}
