package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.Application;
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
        return applicationRepository.save(applicationRequest.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long updateApplication(ApplicationRequest applicationRequest) {
        Application application = applicationRepository.findByNameAndStudentNumber(applicationRequest.getName(), applicationRequest.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 존재하지 않습니다."));
        application.updateApplication(applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }
}
