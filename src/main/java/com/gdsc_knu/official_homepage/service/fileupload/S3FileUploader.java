package com.gdsc_knu.official_homepage.service.fileupload;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
@Profile({"prod","test","local"})
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader{
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        // 원본의 확장자만 추출하여 고유한 파일 이름 설정
        String filename = file.getOriginalFilename();
        String extension =
                filename != null ? filename.substring(filename.lastIndexOf("."))
                        : "";
        String uniqueFilename = UUID.randomUUID() + extension;

        // 파일의 InputStream을 가져와 업로드
        try (InputStream inputStream = file.getInputStream()) {
            // S3에 업로드할 파일의 메타데이터를 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3Client.putObject(new PutObjectRequest(bucket, uniqueFilename, inputStream, metadata));
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }

        return amazonS3Client.getUrl(bucket, uniqueFilename).toString();
    }
}
