package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;

public interface ApplicationService {
    ApplicationResponse getApplication(String email, String name, String studentNumber, Long classYearId);

    Long saveApplication(String email, ApplicationRequest applicationRequest);

    Long updateApplication(String email, ApplicationRequest applicationRequest);

}
