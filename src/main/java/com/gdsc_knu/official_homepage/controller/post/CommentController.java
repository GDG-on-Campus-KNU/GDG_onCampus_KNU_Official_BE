package com.gdsc_knu.official_homepage.controller.post;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.CommentRequest;
import com.gdsc_knu.official_homepage.dto.post.CommentResponse;
import com.gdsc_knu.official_homepage.service.post.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "게시글 댓글 관련 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("{postId}/comment")
    @Operation(summary = "댓글 작성 API", description = "parentId가 null or 0이면 댓글, 아니면 해당 댓글의 답글로 저장한다.")
    public ResponseEntity<Void> createComment(
            @TokenMember JwtMemberDetail jwtMemberDetail,
            @PathVariable(name = "postId")Long postId,
            @RequestBody CommentRequest.Create request)
    {
        commentService.createComment(jwtMemberDetail.getId(), postId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("{postId}/comment")
    @Operation(summary = "댓글 조회 API", description = "댓글의 답글까지 포함한 개수로 페이징 조회한다.")
    public ResponseEntity<PagingResponse<CommentResponse>> getComment(
            Authentication authentication,
            @PathVariable(name = "postId")Long postId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size)
    {
        Long memberId = 0L;
        if (authentication != null){
            JwtMemberDetail jwtMemberDetail = (JwtMemberDetail) authentication.getPrincipal();
            memberId = jwtMemberDetail.getId();
        }
        return ResponseEntity.ok().body(commentService.getComment(memberId, postId, PageRequest.of(page,size)));
    }



}
