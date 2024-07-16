package com.gdsc_knu.official_homepage.dto.member;

import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.Getter;

@Getter
public class MemberInfoAdd {
    private String name;
    private int age;
    private String studentNumber;
    private String major;
    private String phoneNumber;
    private Track track;
}
