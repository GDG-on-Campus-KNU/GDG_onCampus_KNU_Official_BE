package com.gdsc_knu.official_homepage.dto.fileupload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class FileUploadResponse {
    private final String imageUrl;

    private FileUploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static FileUploadResponse of(String imageUrl) {
        return new FileUploadResponse(imageUrl);
    }
}
