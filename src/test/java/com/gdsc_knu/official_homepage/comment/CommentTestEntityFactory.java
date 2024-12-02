package com.gdsc_knu.official_homepage.comment;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;

import java.util.Optional;

public class CommentTestEntityFactory {
    public static Optional<Post> createPost(long postId, long authorId) {
        return Optional.ofNullable(Post.builder()
                .id(postId)
                .member(createAuthor(authorId).get())
                .build());
    }

    public static Comment createComment(long postId, long authorId) {
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

    public static Optional<Member> createAuthor(long id) {
        return Optional.ofNullable(Member.builder()
                .id(id)
                .email("email@email.com")
                .phoneNumber("010-0000-0000")
                .name("테스트 유저")
                .profileUrl("image.png")
                .build());
    }
}
