package com.gdsc_knu.official_homepage.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Component
public class ImageUploader {
    public String upload(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String path = "/home/ubuntu/images";

        UUID uuid = UUID.randomUUID();
        String savedFilename = uuid.toString() + "_" + originalFilename;
        File file = new File(savedFilename);
        try {
            image.transferTo(file);
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }

        return savedFilename;
    }
}
