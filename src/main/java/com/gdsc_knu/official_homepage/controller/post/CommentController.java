package com.gdsc_knu.official_homepage.controller.post;

import com.gdsc_knu.official_homepage.annotation.TokenMember;
import com.gdsc_knu.official_homepage.authentication.jwt.JwtMemberDetail;
import com.gdsc_knu.official_homepage.dto.post.CommentRequest;
import com.gdsc_knu.official_homepage.service.post.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody CommentRequest.Create request
    ) {
        commentService.createComment(jwtMemberDetail.getId(), postId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
