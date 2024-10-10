package com.gdsc_knu.official_homepage.controller.post;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping()
    @Operation(summary = "게시글 목록 조회 API", description = "게시글 목록을 조회한다. 카테고리별로 조회할 수 있다. 없으면 전체 조회를 한다.")
    public ResponseEntity<List<PostResponse.Main>> getPostList(@RequestParam(required = false) Category category) {
        List<PostResponse.Main> postList = postService.getPostList(category);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "게시글 id로 게시글을 단건 조회한다.")
    public ResponseEntity<PostResponse.Main> getPost(@PathVariable("postId") Long postId) {
        PostResponse.Main post = postService.getPost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "게시글 작성 API", description = "게시글을 작성한다. 회원만 작성 가능하다.")
    public ResponseEntity<Void> createPost(@TokenMember JwtMemberDetail jwtMemberDetail,
                                           @RequestBody PostRequest.Create postRequestDto) {
        postService.createPost(jwtMemberDetail.getId(), postRequestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정 API", description = "게시글을 수정한다. 작성자만 수정 가능하다.")
    public ResponseEntity<Void> updatePost(@TokenMember JwtMemberDetail jwtMemberDetail,
                                           @PathVariable("postId") Long postId,
                                           @RequestBody PostRequest.Update postRequestDto) {
        postService.updatePost(jwtMemberDetail.getId(), postId, postRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "게시글을 삭제한다. 작성자 혹은 관리자(Core)만 삭제 가능하다.")
    public ResponseEntity<Void> deletePost(@TokenMember JwtMemberDetail jwtMemberDetail,
                                           @PathVariable("postId") Long postId) {
        postService.deletePost(jwtMemberDetail.getId(), postId);
        return ResponseEntity.ok().build();
    }
}
