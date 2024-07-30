package com.gdsc_knu.official_homepage.authentication.jwt;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtClaims {
    private String email;
    private Role role;
    private Long id;
}
