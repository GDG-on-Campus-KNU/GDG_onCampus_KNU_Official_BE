package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ApplicationRepository;
import com.gdsc_knu.official_homepage.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {
    private final ApplicationRepository applicationRepository;
    private final MailService mailService;
    private final TransactionTemplate transactionTemplate;


    @Transactional(readOnly = true)
    public AdminApplicationResponse.Statistics getStatistic(Long classYearId) {
        ApplicationStatisticType statistic = applicationRepository.getStatistics(classYearId);
        return AdminApplicationResponse.Statistics.from(statistic);
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> getTrackStatistic(Long classYearId) {
        List<ApplicationTrackType> trackStatistics = applicationRepository.getGroupByTrack(classYearId);

        Map<String, Integer> trackCountMap = trackStatistics.stream()
                .collect(Collectors.toMap(ApplicationTrackType::getTrack, ApplicationTrackType::getCount));

        addDefaultTrack(trackCountMap);
        addTotalCount(trackCountMap);
        return trackCountMap;
    }

    private void addDefaultTrack(Map<String, Integer> trackCountMap){
        Arrays.stream(Track.getValidTrack())
                .forEach(track -> trackCountMap.putIfAbsent(track.name(), 0));
    }

    private void addTotalCount(Map<String, Integer> trackCountMap){
        int totalCount = trackCountMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        trackCountMap.put("TOTAL", totalCount);
    }



    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByOption(int page, int size, Track track, Boolean isMarked, Long classYearId){
        Page<Application> applicationPage
                = applicationRepository.findAllApplicationsByOption(PageRequest.of(page,size), track, isMarked, classYearId);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }

    @Transactional
    public void markApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        application.changeMark();
    }


    /**
     * 메일 전송에 실패해도 status 롤백하지 않는다.
     * 관리자 기능이므로 비동기로 처리하지 않고, 실행결과를 알려준다.
     */
    public void decideApplication(Long id, ApplicationStatus applicationStatus) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        transactionTemplate.executeWithoutResult(
                status -> application.updateStatus(applicationStatus)
        );
        mailService.sendEach(application);
    }


    @Transactional
    public AdminApplicationResponse.Detail getApplicationDetail(Long id) {
        Application application = applicationRepository.findByIdFetchJoin(id)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        application.open();
        return AdminApplicationResponse.Detail.from(application);
    }

    @Transactional
    public void noteApplication(Long id, String note, Integer version) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        application.saveNote(note, version);

        try {
            applicationRepository.saveAndFlush(application);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomException(ErrorCode.CONCURRENT_FAILED);
        }
    }


    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByName(int page, int size, String name){
        Page<Application> applicationPage
                = applicationRepository.findByNameContaining(PageRequest.of(page,size), name);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }
}
