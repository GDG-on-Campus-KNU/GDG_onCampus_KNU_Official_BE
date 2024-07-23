package com.gdsc_knu.official_homepage.service.fileupload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String upload(MultipartFile file);
}
