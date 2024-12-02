package com.gdsc_knu.official_homepage.comment.domain;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gdsc_knu.official_homepage.comment.CommentTestEntityFactory.createAuthor;
import static com.gdsc_knu.official_homepage.comment.CommentTestEntityFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    @DisplayName("댓글이 저장될때 게시글의 댓글 개수 필드가 정상적으로 업데이트되고, 게시글 댓글 컬렉션에 포함된다.")
    void saveCommentCount() {
        // given
        Member author = createAuthor(1L).orElseThrow();
        Post post = createPost(1L, author.getId()).orElseThrow();
        // when
        Comment comment1 = Comment.from("댓글 내용", author, post, null);
        // then
        assertThat(post.getCommentCount()).isEqualTo(1);
        assertThat(post.getCommentList()).containsExactly(comment1);
    }

    @Test
    @DisplayName("상위 댓글 삭제 시 연관된 하위 댓글 삭제까지의 댓글개수가 정상적으로 업데이트된다.")
    void deleteParentComment() {
        // given
        Member author = createAuthor(1L).orElseThrow();
        Post post = createPost(1L, author.getId()).orElseThrow();
        Comment parent = Comment.from("댓글 내용", author, post, null);
        Comment child1 = Comment.from("댓글 내용", author, post, parent);
        Comment child2 = Comment.from("댓글 내용", author, post, parent);
        // when
        parent.delete();

        //then
        assertThat(post.getCommentCount()).isEqualTo(0);
    }
}
