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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Post", description = "테크블로그 게시글 관련 API")
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

    @GetMapping("/temporal")
    @Operation(summary = "임시 저장 게시글 목록 조회 API", description = "임시 저장 게시글 목록을 조회한다. 본인의 임시 저장 게시글만 조회할 수 있다.")
    public ResponseEntity<List<PostResponse.Temp>> getTemporalPostList(@TokenMember JwtMemberDetail jwtMemberDetail) {
        List<PostResponse.Temp> postList = postService.getTemporalPostList(jwtMemberDetail.getId());
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/temporal/{postId}")
    @Operation(summary = "임시 저장 게시글 목록 조회 API", description = "임시 저장 게시글 목록을 조회한다. 본인의 임시 저장 게시글만 조회할 수 있다.")
    public ResponseEntity<List<PostResponse.Temp>> getTemporalPost(@PathVariable("postId") Long postId,
                                                                   @TokenMember JwtMemberDetail jwtMemberDetail) {
        List<PostResponse.Temp> postList = postService.getTemporalPostList(jwtMemberDetail.getId());
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "게시글 id로 게시글을 단건 조회한다.")
    public ResponseEntity<PostResponse.Detail> getPost(@PathVariable("postId") Long postId) {
        PostResponse.Detail post = postService.getPost(postId);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping
    @Operation(summary = "게시글 작성 API", description = "게시글을 작성한다. 회원만 작성 가능하다.")
    public ResponseEntity<Void> createPost(@TokenMember JwtMemberDetail jwtMemberDetail,
                                           @RequestBody PostRequest.Create postRequestDto) {
        postService.createPost(jwtMemberDetail.getId(), postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
  
    @GetMapping("trending")
    @Operation(summary = "카테고리별 인기글 5개 조회", description = "category가 null이면 전제를 조회한다.")
    public ResponseEntity<List<PostResponse.Main>> getTrendingPosts(
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "size", defaultValue = "5") int size)
    {
        return ResponseEntity.ok().body(postService.getTrendingPosts(category, size));
    }
}
