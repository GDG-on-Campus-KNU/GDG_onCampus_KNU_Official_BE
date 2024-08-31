package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public AdminApplicationResponse.Statistics getStatistic() {
        ApplicationStatisticType statistic = applicationRepository.getStatistics();
        return AdminApplicationResponse.Statistics.from(statistic);
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> getTrackStatistic() {
        List<ApplicationTrackType> trackStatistics = applicationRepository.getGroupByTrack();

        Map<String, Integer> trackCountMap = trackStatistics.stream()
                .collect(Collectors.toMap(ApplicationTrackType::getTrack, ApplicationTrackType::getCount));

        addDefaultTrack(trackCountMap);
        addTotalCount(trackCountMap);
        return trackCountMap;
    }

    private void addDefaultTrack(Map<String, Integer> trackCountMap){
        Arrays.stream(Track.values())
                .forEach(track -> trackCountMap.putIfAbsent(track.name(), 0));
    }

    private void addTotalCount(Map<String, Integer> trackCountMap){
        int totalCount = trackCountMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        trackCountMap.put("TOTAL", totalCount);
    }



    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByOption(int page, int size, Track track, boolean isMarked){
        Page<Application> applicationPage
                = applicationRepository.findAllApplicationsByOption(PageRequest.of(page,size), track, isMarked);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }

    @Transactional
    public void markApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        application.changeMark();
    }


    //TODO: 메일 발송 부분 트랜젝션 밖으로 이동 필요
    @Transactional
    public void decideApplication(Long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        if (status == ApplicationStatus.APPROVED){
            application.approve();
            mailService.sendOne(application);
        }

        else if (status == ApplicationStatus.REJECTED){
            application.reject();
            mailService.sendOne(application);
        }
    }


    @Transactional
    public AdminApplicationResponse.Detail getApplicationDetail(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        if (!application.isOpened())
            application.open();
        return AdminApplicationResponse.Detail.from(application);
    }

    @Transactional
    public void noteApplication(Long id, String note) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        application.saveNote(note);
    }


    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByName(int page, int size, String name){
        Page<Application> applicationPage
                = applicationRepository.findByNameContaining(PageRequest.of(page,size), name);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }
}
