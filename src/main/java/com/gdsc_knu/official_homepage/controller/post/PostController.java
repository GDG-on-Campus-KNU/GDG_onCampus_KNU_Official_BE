package com.gdsc_knu.official_homepage.controller.post;

import com.gdsc_knu.official_homepage.dto.fileupload.FileUploadRequest;
import com.gdsc_knu.official_homepage.dto.fileupload.FileUploadResponse;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.service.fileupload.FileUploader;
import com.gdsc_knu.official_homepage.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "테크블로그 관련 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final FileUploader fileUploader;
    @GetMapping("trending")
    @Operation(summary = "카테고리별 인기글 5개 조회", description = "category가 null이면 전제를 조회한다.")
    public ResponseEntity<List<PostResponse.Main>> getTrendingPosts(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "size", defaultValue = "5") int size)
    {
        return ResponseEntity.ok().body(postService.getTrendingPosts(category, size));
    }

    @PostMapping(value = "image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 업로드")
    public ResponseEntity<FileUploadResponse> uploadImage(@Valid @ModelAttribute FileUploadRequest.Create request) {
        String url = fileUploader.upload(request.getImage(), "post");
        return ResponseEntity.ok().body(FileUploadResponse.of(url));
    }

}
