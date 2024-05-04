package com.gdsc_knu.official_homepage.service.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.dto.application.ApplicationResponse;

public interface ApplicationService {
    ApplicationResponse getApplication(String name, String studentNumber);

    Long saveApplication(ApplicationRequest applicationRequest);

    Long updateApplication(ApplicationRequest applicationRequest);

}
