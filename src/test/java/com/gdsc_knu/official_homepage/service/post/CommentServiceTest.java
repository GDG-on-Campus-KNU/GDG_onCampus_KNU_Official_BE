package com.gdsc_knu.official_homepage.service.post;

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


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks private CommentService commentService;
    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private MemberRepository memberRepository;

    private final Long memberId = 1L;
    private final Long postId = 1L;
    private final Long postAuthorId = 2L;
    private final Long commentAuthorId = 3L;

    @Test
    @DisplayName("댓글이 정상적으로 저장된다")
    void saveComment() {
        // given
        when(postRepository.findById(postId)).thenReturn(createPost());
        when(memberRepository.findById(memberId)).thenReturn(createAuthor(memberId));
        // when
        commentService.createComment(memberId, postId, new CommentRequest.Create(null, "댓글 내용"));
        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 답글을 남기면 오류를 발생시킨다.")
    void saveCommentInvalidParent() {
        // given
        when(postRepository.findById(postId)).thenReturn(createPost());
        when(memberRepository.findById(memberId)).thenReturn(createAuthor(memberId));
        // when
        Long fakeParentId = 1L;
        Exception exception = assertThrows(CustomException.class, () ->
            commentService.createComment(memberId, postId, new CommentRequest.Create(fakeParentId, "댓글 내용"))
        );
        //then
        assertEquals(ErrorCode.COMMENT_NOT_FOUND.getMessage(), exception.getMessage());
    }

    //TODO: 통합테스트
    @Test
    @DisplayName("댓글의 순서가 올바르게 조회된다")
    void getComment() {

    }

    @Test
    @DisplayName("댓글 작성자는 본인이 작성한 댓글을 수정, 삭제할 수 있다.")
    void getCommentByCommentAuthor() {
        // given
        long commentAuthorId = memberId;
        when(postRepository.findById(postId)).thenReturn(createPostWithAuthor(postAuthorId));
        Comment comment = createComment(commentAuthorId);
        when(commentRepository.findCommentAndReply(PageRequest.of(0,5), postId))
                .thenReturn(new PageImpl<>(Collections.singletonList(comment)));
        // when
        PagingResponse<CommentResponse> response = commentService.getComment(memberId, postId, PageRequest.of(0,5));

        // then
        assertTrue(response.getData().get(0).isCanModify());
        assertTrue(response.getData().get(0).isCanDelete());
    }

    @Test
    @DisplayName("게시글 작성자는 해당 게시글의 댓글을 삭제할 수 있고, 수정할 수 없다.")
    void getCommentByPostAuthor() {
        //given
        long postAuthorId = memberId;
        when(postRepository.findById(postId)).thenReturn(createPostWithAuthor(postAuthorId));
        Comment comment = createComment(commentAuthorId);
        when(commentRepository.findCommentAndReply(PageRequest.of(0,5), postId))
                .thenReturn(new PageImpl<>(Collections.singletonList(comment)));
        // when
        PagingResponse<CommentResponse> response = commentService.getComment(memberId, postId, PageRequest.of(0,5));

        // then
        assertFalse(response.getData().get(0).isCanModify());
        assertTrue(response.getData().get(0).isCanDelete());
    }







    public Optional<Post> createPost() {
        return Optional.ofNullable(Post.builder()
                .id(postId)
                .build());
    }

    public Optional<Post> createPostWithAuthor(long authorId) {
        Member member = createAuthor(authorId).get();
        return Optional.ofNullable(Post.builder()
                .id(postId)
                .member(member)
                .build());
    }

    public Comment createComment(long authorId) {
        Member author = createAuthor(authorId).get();
        return Comment.builder()
                .id(postId)
                .content("댓글")
                .author(author)
                .parent(fakeParentComment())
                .build();
    }

    public Comment fakeParentComment() {
        return Comment.builder()
                .id(1L)
                .build();
    }

    public Optional<Member> createAuthor(long id) {
        return Optional.ofNullable(Member.builder()
                .id(id)
                .email("email@email.com")
                .phoneNumber("010-0000-0000")
                .name("테스트 유저")
                .profileUrl("image.png")
                .build());
    }
}