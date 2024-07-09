package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String name, String studentNumber) {
        Application application = applicationRepository.findByNameAndStudentNumber(name, studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));
        return new ApplicationResponse(application);
    }

    @Override
    @Transactional
    public Long saveApplication(ApplicationRequest applicationRequest) {
        applicationRepository.findByNameAndStudentNumber(applicationRequest.getName(), applicationRequest.getStudentNumber()).ifPresent(application -> {
            throw new IllegalArgumentException("지원자님의 지원서가 이미 존재합니다.");
        });
        return applicationRepository.save(applicationRequest.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long updateApplication(ApplicationRequest applicationRequest) {
        Application application = applicationRepository.findByNameAndStudentNumber(applicationRequest.getName(), applicationRequest.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 존재하지 않습니다."));
        if (application.getApplicationStatus().equals(ApplicationStatus.SAVED)) {
            throw new IllegalArgumentException("최종 제출 된 지원서는 수정할 수 없습니다.");
        }
        application.updateApplication(applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }
}
