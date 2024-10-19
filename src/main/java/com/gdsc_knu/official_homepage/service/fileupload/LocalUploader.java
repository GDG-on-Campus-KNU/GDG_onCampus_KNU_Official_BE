package com.gdsc_knu.official_homepage.service.fileupload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Profile({"!dev && !prod"})
public class LocalUploader implements FileUploader {
    @Value("${local.storage.path}")
    private String uploadPath;
    @Override
    public String upload(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();

        String savedFilename = uploadPath + UUID.randomUUID() + "_" + originalFilename;
        File savedFile = new File(savedFilename);
        try {
            file.transferTo(savedFile);
        } catch (IOException e) {
            log.error("파일 업로드에 실패하여 재시도합니다.");
            throw new RuntimeException();
        }

        return savedFilename;
    }
}
