package com.gdsc_knu.official_homepage.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;


    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000),
            recover = "recover"
    )
    public String upload(MultipartFile file, String username) {
        // 원본의 확장자만 추출하여 고유한 파일 이름 설정
        String filename = getFilePath(file.getOriginalFilename(), username);

        // 파일의 InputStream을 가져와 업로드
        try (InputStream inputStream = file.getInputStream()) {
            // S3에 업로드할 파일의 메타데이터를 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, metadata));

        } catch (IOException e) {
            log.warn("이미지 업로드에 실패하여 다시 시도합니다.");
            throw new CustomException(ErrorCode.FAILED_UPLOAD);
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }


    private String getFilePath(String filename, String username) {
        String extension = filename != null
                ? filename.substring(filename.lastIndexOf("."))
                : "";
        String uniqueFilename = username + "-" + UUID.randomUUID();
        String path = activeProfile + "/";
        return path + uniqueFilename + extension;
    }


    @Recover
    private String recover(Exception e, MultipartFile file){
        throw new CustomException(ErrorCode.FAILED_UPLOAD);
    }
}
