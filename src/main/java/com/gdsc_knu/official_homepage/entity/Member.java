package com.gdsc_knu.official_homepage.entity;

import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    private String name;

    private int age;

    private String studentNumber;

    private String major;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String sessionId;

    public void updateSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
