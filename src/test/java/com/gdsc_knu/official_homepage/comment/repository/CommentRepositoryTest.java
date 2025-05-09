package com.gdsc_knu.official_homepage.comment.repository;

import com.gdsc_knu.official_homepage.ClearDatabase;
import com.gdsc_knu.official_homepage.config.QueryDslConfig;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.repository.post.CommentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;


@DataJpaTest
@Import({QueryDslConfig.class, ClearDatabase.class})
public class CommentRepositoryTest {
    @Autowired private CommentRepository commentRepository;
    @Autowired private TestEntityManager entityManager;
    @Autowired private ClearDatabase clearDatabase;

    private Member author;
    private Post post;
    @BeforeEach
    void setUp() {
        author = Member.builder()
                .email("test@email.com")
                .name("테스트 유저")
                .track(Track.BACK_END)
                .build();
        entityManager.persist(author);

        post = Post.builder()
                .title("제목")
                .member(author)
                .build();
        entityManager.persist(post);
    }

    @AfterEach
    void tearDown() {
        clearDatabase.each("comment");
        clearDatabase.each("post");
        clearDatabase.each("member");
    }

    @Test
    @DisplayName("댓글의 순서가 올바르게 조회된다")
    void findCommentAndReply() {
        // given
        Comment parent1 = Comment.from("1",author,post,null);
        Comment child1 = Comment.from("2",author,post,parent1);
        Comment child2 = Comment.from("3",author,post,parent1);
        Comment parent2 = Comment.from("4",author,post,null);
        Comment child3 = Comment.from("5",author,post,parent2);
        Comment child4 = Comment.from("6",author,post,parent2);

        commentRepository.saveAll(List.of(parent1, parent2, child1, child2, child3, child4));
        // 2,5 댓글의 생성 시각 나중으로 변경
        ReflectionTestUtils.setField(child3, "createAt", LocalDateTime.now());
        ReflectionTestUtils.setField(child1, "createAt", LocalDateTime.now());

        // when
        List<Comment> commentList = commentRepository.findCommentAndReply(PageRequest.of(0,6), post.getId());

        // then
        assertThat(commentList).extracting("content")
                .containsExactly("1","3","2","4","6","5");
    }


    @Test
    @DisplayName("부모 댓글 생성 시 자신을 그룹으로 한다.")
    void save() {
        // given
        Comment parent = Comment.from("댓글내용",author,post,null);
        Comment child = Comment.from("댓글내용",author,post,parent);

        // when
        commentRepository.saveAll(List.of(parent, child));

        // then
        assertThat(parent.getParent()).isEqualTo(parent);
        assertThat(child.getParent()).isEqualTo(parent);
    }


}
