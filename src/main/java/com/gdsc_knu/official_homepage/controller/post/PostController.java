package com.gdsc_knu.official_homepage.controller.post;


import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import com.gdsc_knu.official_homepage.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "테크블로그 게시글 관련 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping()
    @Operation(summary = "게시글 목록 조회 API", description = "게시글 목록을 조회한다. 카테고리별로 조회할 수 있다. 없으면 전체 조회를 한다.")
    public ResponseEntity<PagingResponse<PostResponse.Main>> getPostList(@RequestParam(value = "category", required = false) Category category,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "20") int size) {
        PagingResponse<PostResponse.Main> postList = postService.getPostList(category, page, size);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/mypost")
    @Operation(summary = "본인 게시글 목록 조회 API", description = "본인의 게시글 목록을 조회한다. 조회할 게시글 상태가 필요하다.")
    public ResponseEntity<PagingResponse<PostResponse.Temp>> getTemporalPostList(@TokenMember JwtMemberDetail jwtMemberDetail,
                                                                       @RequestParam(value = "status") PostStatus status,
                                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        PagingResponse<PostResponse.Temp> postList = postService.getTemporalPostList(jwtMemberDetail.getId(), status, page, size);
        return ResponseEntity.ok().body(postList);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "게시글 id로 게시글을 단건 조회한다.")
    public ResponseEntity<PostResponse.Detail> getPost(Authentication authentication,
                                                       @PathVariable("postId") Long postId) {
        Long memberId = 0L;
        if (authentication != null){
            JwtMemberDetail jwtMemberDetail = (JwtMemberDetail) authentication.getPrincipal();
            memberId = jwtMemberDetail.getId();
        }

        PostResponse.Detail post = postService.getPost(memberId, postId);
        return ResponseEntity.ok().body(post);
    }

//    @GetMapping("/{postId}/modify")
//    @Operation(summary = "게시글 수정용 정보 조회 API", description = "게시글 수정을 위해 필요한 정보를 조회한다. 작성자만 조회 가능하다.")
//    public ResponseEntity<PostResponse.Modify> getTemporalPost(@PathVariable("postId") Long postId,
//                                                               @TokenMember JwtMemberDetail jwtMemberDetail) {
//        PostResponse.Modify modifyPost = postService.getModifyPost(jwtMemberDetail.getId(), postId);
//        return ResponseEntity.ok().body(modifyPost);
//    }
    @PostMapping()
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

    @GetMapping("/search")
    @Operation(summary = "검색 API", description = "제목, 부제목, 본문 내용에 키워드 포함 여부로 게시글을 검색한다.")
    public ResponseEntity<PagingResponse<PostResponse.Main>> searchPosts(@RequestParam(value = "keyword") String keyword,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(postService.searchPostList(keyword, page, size));
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
