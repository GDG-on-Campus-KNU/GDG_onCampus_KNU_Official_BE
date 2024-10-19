package com.gdsc_knu.official_homepage.dto.fileupload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    @Getter
    @AllArgsConstructor
    public static class Create{
        private MultipartFile image;
    }
}
