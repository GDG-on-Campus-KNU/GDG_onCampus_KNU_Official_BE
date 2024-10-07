package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.CommentRequest;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.CommentRepository;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks private CommentService commentService;
    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private MemberRepository memberRepository;


    private final Long memberId = 1L;
    private final Long postId = 1L;

    @Test
    @DisplayName("댓글이 정상적으로 저장된다")
    void createComment() {
        // given
        when(postRepository.findById(postId)).thenReturn(createPost());
        when(memberRepository.findById(memberId)).thenReturn(createAuthor());

        // when
        commentService.createComment(memberId, postId, new CommentRequest.Create(null, "댓글 내용"));

        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 답글을 남기면 오류를 발생시킨다.")
    void createCommentInvalidParent() {
        // given
        when(postRepository.findById(postId)).thenReturn(createPost());
        when(memberRepository.findById(memberId)).thenReturn(createAuthor());

        // when
        Long fakeParentId = 1L;

        Exception exception = assertThrows(CustomException.class, () ->
            commentService.createComment(memberId, postId, new CommentRequest.Create(fakeParentId, "댓글 내용"))
        );

        //then
        assertEquals(ErrorCode.COMMENT_NOT_FOUND.getMessage(), exception.getMessage());
    }







    public Optional<Post> createPost() {
        return Optional.ofNullable(Post.builder()
                .id(postId)
                .build());
    }

    public Optional<Member> createAuthor() {
        return Optional.ofNullable(Member.builder()
                .id(memberId)
                .email("email@email.com")
                .phoneNumber("010-0000-0000")
                .name("테스트 유저")
                .profileUrl("image.png")
                .build());
    }
}