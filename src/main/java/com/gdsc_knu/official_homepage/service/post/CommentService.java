package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.CommentRequest;
import com.gdsc_knu.official_homepage.dto.post.CommentResponse;
import com.gdsc_knu.official_homepage.dto.post.AccessModel;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.post.CommentRepository;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse createComment(Long memberId, Long postId, CommentRequest.Create request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment parent = getParentComment(request.getGroupId());
        Comment comment = Comment.from(request.getContent(), member, post, parent);
        commentRepository.save(comment);
        return CommentResponse.fromCreator(comment);
    }

    private Comment getParentComment(Long parentId) {
        if (parentId == null || parentId.equals(0L)) {
            return null;
        }
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (parent.isChild()) {
            throw new CustomException(ErrorCode.INVALID_COMMENT);
        }
        return parent;
    }



    @Transactional(readOnly = true)
    public PagingResponse<CommentResponse> getComment(Long memberId, Long postId, PageRequest pageRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long postAuthorId = post.getMember().getId();

        List<Comment> commentList = commentRepository.findCommentAndReply(pageRequest, postId);
        return createPagingResponse(commentList, memberId, postAuthorId, pageRequest.getPageSize());
    }

    private PagingResponse<CommentResponse> createPagingResponse(List<Comment> commentList, Long memberId, Long postAuthorId, int size) {
        return PagingResponse.withoutCountFrom(commentList, size, comment -> {
            Long commentAuthorId = comment.getAuthor().getId();
            AccessModel access = AccessModel.calcCommentAccess(memberId, postAuthorId, commentAuthorId);
            return CommentResponse.from(comment, access);
        });
    }


    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentRequest.Update request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.isCommentAuthor(memberId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
        comment.update(request.getContent());
    }

    //TODO: 대댓글이 많은 경우 bulk delete 고려
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        if (!comment.isCommentAuthor(memberId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
        comment.delete();
        commentRepository.delete(comment);
    }
}
