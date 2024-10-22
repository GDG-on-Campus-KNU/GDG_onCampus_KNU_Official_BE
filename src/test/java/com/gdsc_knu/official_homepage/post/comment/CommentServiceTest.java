package com.gdsc_knu.official_homepage.post.comment;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.CommentRequest;
import com.gdsc_knu.official_homepage.dto.post.CommentResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.CommentRepository;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import com.gdsc_knu.official_homepage.service.post.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks private CommentService commentService;
    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private MemberRepository memberRepository;

    private final Long memberId = 1L;
    private final Long postId = 1L;
    private final Optional<Member> author = createAuthor(memberId);
    private final Optional<Post> post = createPost(memberId);

    @Test
    @DisplayName("댓글이 정상적으로 저장된다")
    void saveComment() {
        // given
        when(postRepository.findById(postId)).thenReturn(post);
        when(memberRepository.findById(memberId)).thenReturn(author);
        // when
        commentService.createComment(memberId, postId, new CommentRequest.Create(null, "댓글 내용"));
        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 답글을 남기면 오류를 발생시킨다.")
    void saveCommentInvalidParent() {
        // given
        when(postRepository.findById(postId)).thenReturn(createPost(memberId));
        when(memberRepository.findById(memberId)).thenReturn(createAuthor(memberId));
        Long notExistCommentId = 1L;
        CommentRequest.Create request = new CommentRequest.Create(notExistCommentId, "댓글 내용");

        // when && then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() ->
                        commentService.createComment(memberId, postId, request)
                ).withMessage(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }


    @Test
    @DisplayName("댓글 작성자는 본인이 작성한 댓글을 수정, 삭제할 수 있다.")
    void getCommentByCommentAuthor() {
        // given (댓글을 본인이 작성했으나, 게시글은 본인이 작성한게 아닌 경우)
        long postAuthorId = 2L;
        when(postRepository.findById(postId)).thenReturn(createPost(postAuthorId));
        Comment comment = createComment(memberId);
        when(commentRepository.findCommentAndReply(PageRequest.of(0,5), postId))
                .thenReturn(new PageImpl<>(Collections.singletonList(comment)));
        // when
        PageRequest page = PageRequest.of(0,5);
        PagingResponse<CommentResponse> response = commentService.getComment(memberId, postId, page);

        // then
        assertTrue(response.getData().get(0).isCanModify());
        assertTrue(response.getData().get(0).isCanDelete());
    }

    @Test
    @DisplayName("게시글 작성자는 해당 게시글의 댓글을 삭제할 수 있고, 수정할 수 없다.")
    void getCommentByPostAuthor() {
        //given (본인의 게시글에 다른 사람의 댓글이 존재하는 경우)
        long commentAuthorId = 3L;
        when(postRepository.findById(postId)).thenReturn(createPost(memberId));
        Comment comment = createComment(commentAuthorId);
        when(commentRepository.findCommentAndReply(PageRequest.of(0,5), postId))
                .thenReturn(new PageImpl<>(Collections.singletonList(comment)));
        // when
        PageRequest page = PageRequest.of(0,5);
        PagingResponse<CommentResponse> response = commentService.getComment(memberId, postId, page);

        // then
        assertFalse(response.getData().get(0).isCanModify());
        assertTrue(response.getData().get(0).isCanDelete());
    }







    private Optional<Post> createPost(long authorId) {
        return Optional.ofNullable(Post.builder()
                .id(postId)
                .member(createAuthor(authorId).get())
                .build());
    }

    private Comment createComment(long authorId) {
        Member author = createAuthor(authorId).get();
        Comment parent = Comment.builder()
                .id(1L)
                .build();
        return Comment.builder()
                .id(postId)
                .content("댓글")
                .author(author)
                .parent(parent)
                .build();
    }

    private Optional<Member> createAuthor(long id) {
        return Optional.ofNullable(Member.builder()
                .id(id)
                .email("email@email.com")
                .phoneNumber("010-0000-0000")
                .name("테스트 유저")
                .profileUrl("image.png")
                .build());
    }
}