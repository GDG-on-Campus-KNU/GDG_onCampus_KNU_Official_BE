package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getApplication(String name, String studentNumber) {
        Application application = applicationRepository.findByNameAndStudentNumber(name, studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.ALREADY_SAVED_APPLICATION);
        }
        return new ApplicationResponse(application);
    }

    @Override
    @Transactional
    public Long saveApplication(ApplicationRequest applicationRequest) {
        checkValidApplicationStatus(applicationRequest.getApplicationStatus());
        applicationRepository.findByStudentNumber(applicationRequest.getStudentNumber())
                .ifPresent(application -> {
                    throw new IllegalArgumentException("이미 존재하는 지원서입니다.");
                });
        return applicationRepository.save(applicationRequest.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long updateApplication(ApplicationRequest applicationRequest) {
        checkValidApplicationStatus(applicationRequest.getApplicationStatus());
        Application application = applicationRepository.findByNameAndStudentNumber(applicationRequest.getName(), applicationRequest.getStudentNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 존재하지 않습니다."));
        if (!application.getApplicationStatus().equals(ApplicationStatus.TEMPORAL)) {
            throw new CustomException(ErrorCode.ALREADY_SAVED_APPLICATION);
        }
        application.updateApplication(applicationRequest);
        applicationRepository.save(application);
        return application.getId();
    }

    private void checkValidApplicationStatus(ApplicationStatus applicationStatus) {
        if (applicationStatus.equals(ApplicationStatus.REJECTED) || applicationStatus.equals(ApplicationStatus.APPROVED)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }
}
