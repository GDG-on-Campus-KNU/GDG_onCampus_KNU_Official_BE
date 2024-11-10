package com.gdsc_knu.official_homepage.post.comment;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.PostLike;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.repository.post.PostLikeRepository;
import com.gdsc_knu.official_homepage.repository.post.PostRepository;
import com.gdsc_knu.official_homepage.service.post.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks private PostServiceImpl postService;
    @Mock private PostRepository postRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private PostLikeRepository postLikeRepository;

    @Test
    @DisplayName("좋아요 추가 시 좋아요 개수가 변경된다.")
    void addPostLike() {
        // given
        Member member = createAuthor();
        Post post = createPost(member);
        when(postLikeRepository.findByMemberIdAndPostId(member.getId(), post.getId())).thenReturn(Optional.empty());
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        // when
        postService.likePost(member.getId(), post.getId());
        // then
        assertThat(post.getLikeCount()).isEqualTo(1L);
    }




    private Post createPost(Member member) {
        return Post.builder()
                .id(1L)
                .member(member)
                .build();
    }

    private Member createAuthor() {
        return Member.builder()
                .id(1L)
                .email("email@email.com")
                .phoneNumber("010-0000-0000")
                .name("테스트 유저")
                .profileUrl("image.png")
                .build();
    }

}
