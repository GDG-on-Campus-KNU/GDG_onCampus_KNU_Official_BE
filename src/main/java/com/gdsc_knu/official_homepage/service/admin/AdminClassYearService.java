package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationRequest;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.application.ClassYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminClassYearService {
    private final ClassYearRepository classYearRepository;

    @Transactional(readOnly = true)
    public List<AdminApplicationResponse.ClassYearResponse> getClassYearList() {
        return classYearRepository.findAll().stream().map(AdminApplicationResponse.ClassYearResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AdminApplicationResponse.ClassYearResponse getClassYear(Long id) {
        ClassYear classYear = classYearRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND));
        return AdminApplicationResponse.ClassYearResponse.from(classYear);
    }

    @Transactional
    public void addClassYear(AdminApplicationRequest.ClassYearRequest request) {
        validateClassYear(request);
        classYearRepository.save(request.toEntity());
    }

    @Transactional
    public void updateClassYear(Long id, AdminApplicationRequest.ClassYearRequest request) {
        ClassYear classYear = classYearRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND));
        validateClassYear(request);
        classYear.update(request.getName(), request.getApplyStartDateTime(), request.getApplyEndDateTime());
        classYearRepository.save(request.toEntity());
    }

    @Transactional
    public void deleteClassYear(Long id) {
        try {
            classYearRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException(ErrorCode.CLASS_YEAR_NOT_FOUND);
        }
    }

    private void validateClassYear(AdminApplicationRequest.ClassYearRequest request) {
        classYearRepository.findByName(request.getName())
                .ifPresent(classYear -> {
                    throw new CustomException(ErrorCode.CLASS_YEAR_DUPLICATED);
                });
        if (request.getApplyStartDateTime().isAfter(request.getApplyEndDateTime()) || request.getApplyEndDateTime().isBefore(request.getApplyStartDateTime())) {
            throw new CustomException(ErrorCode.INVALID_CLASS_YEAR);
        }
    }
}
