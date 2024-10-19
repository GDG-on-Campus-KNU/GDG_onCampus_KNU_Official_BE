package com.gdsc_knu.official_homepage.service.fileupload;

import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000),
            recover = "recover",
            retryFor = {IOException.class, RuntimeException.class}
    )
    String upload(MultipartFile file, String directory);

    @Recover
    default String recover(Exception e, MultipartFile file){
        throw new CustomException(ErrorCode.FAILED_UPLOAD);
    }
}
