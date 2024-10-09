package com.gdsc_knu.official_homepage.controller.post;

import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Post", description = "테크블로그 관련 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping("trending")
    @Operation(summary = "카테고리별 인기글 5개 조회", description = "category가 null이면 전제를 조회한다.")
    public ResponseEntity<List<PostResponse.Main>> getTrendingPosts(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "size", defaultValue = "5") int size)
    {
        return ResponseEntity.ok().body(postService.getTrendingPosts(category, size));
    }

}
