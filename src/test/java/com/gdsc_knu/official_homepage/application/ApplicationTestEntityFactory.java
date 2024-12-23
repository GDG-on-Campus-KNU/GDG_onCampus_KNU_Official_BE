package com.gdsc_knu.official_homepage.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.util.ArrayList;
import java.util.List;

public class ApplicationTestEntityFactory {
    public static Application createApplication(Long id, Track track, ApplicationStatus status) {
        return Application.builder()
                .id(id)
                .email(String.format("test%s@email.com", id))
                .studentNumber(String.valueOf(id))
                .phoneNumber(String.format("010-0000-%s", id))
                .applicationStatus(status)
                .track(track)
                .build();
    }

    public static List<Application> createApplicationList(int startNum, int count, Track track, ApplicationStatus status){
        List<Application> applicationList = new ArrayList<>();
        for (int i=startNum; i<count; i++) {
            applicationList.add(createApplication((long) i, track, status));
        }
        return applicationList;
    }
}
