package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationCheckRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.Application;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Override
    public ApplicationResponse getApplication(ApplicationCheckRequest applicationCheckRequest) {
        Application application = applicationRepository.findByStudentNumber(applicationCheckRequest
                .getStudentNumber()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));
        return new ApplicationResponse(application);
    }

    @Override
    public Long saveApplication(ApplicationRequest applicationRequest) {
        return applicationRepository.save(applicationRequest.toEntity()).getId();
    }

    @Override
    public Long updateApplication(ApplicationRequest applicationRequest) {
        Application application = applicationRepository.findByStudentNumber(applicationRequest.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));
        application.updateApplication(applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }
}
