package com.gdsc_knu.official_homepage.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.dto.application.ApplicationModel;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationTestEntityFactory {
    public static ClassYear createClassYear(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return ClassYear.builder()
                .id(id)
                .name(String.format("test%s기", id))
                .applicationStartDateTime(now)
                .applicationEndDateTime(now.plusDays(1))
                .build();
    }
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

    public static List<ClassYear> createClassYearList(int startNum, int count) {
        List<ClassYear> classYearList = new ArrayList<>();
        for (int i=startNum; i<count; i++) {
            classYearList.add(createClassYear((long) i));
        }
        return classYearList;
    }

    public static void setClassYear(List<Application> applications, List<ClassYear> classYears) {
        for (int i = 0; i < applications.size(); i++) {
            int classYearIdx = i % classYears.size();
            applications.get(i).updateClassYear(classYears.get(classYearIdx));
        }
    }

    public static void setClassYear(List<Application> applications, ClassYear classYear) {
        for (int i = 0; i < applications.size(); i++) {
            applications.get(i).updateClassYear(classYear);
        }
    }

    public static ApplicationRequest createApplicationRequest(Long classYearId) {
        return ApplicationRequest.builder()
                .classYearId(classYearId)
                .techStack("Java")
                .links("https://github.com")
                .track(Track.BACK_END)
                .answers(new ArrayList<>())
                .applicationStatus(ApplicationStatus.TEMPORAL)
                .build();
    }

    public static ApplicationModel createApplicationModel() {
        return ApplicationModel.builder()
                .techStack("Java")
                .links("https://github.com")
                .track(Track.BACK_END)
                .answers(new ArrayList<>())
                .applicationStatus(ApplicationStatus.TEMPORAL)
                .build();
    }

    public static ApplicationModel createApplicationModel(List<ApplicationAnswerDTO> answerList) {
        return ApplicationModel.builder()
                .techStack("Java")
                .links("https://github.com")
                .track(Track.BACK_END)
                .answers(answerList)
                .applicationStatus(ApplicationStatus.TEMPORAL)
                .build();
    }

    public static ApplicationAnswerDTO createApplicationAnswerDTO(int questionNumber, String answer) {
        return ApplicationAnswerDTO.builder()
                .questionNumber(questionNumber)
                .answer(answer)
                .build();
    }

    public static List<ApplicationAnswerDTO> createApplicationAnswerDTOList(int count, String defaultAnswer) {
        List<ApplicationAnswerDTO> answerList = new ArrayList<>();
        for (int i=0; i<count; i++) {
            answerList.add(createApplicationAnswerDTO(i, defaultAnswer+i));
        }
        return answerList;
    }
}
