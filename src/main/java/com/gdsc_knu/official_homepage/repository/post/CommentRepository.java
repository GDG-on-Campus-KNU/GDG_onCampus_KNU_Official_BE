package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId "+
            "ORDER BY c.parent.id ASC, c.createAt ASC")
    List<Comment> findCommentAndReply(Pageable pageable, @Param(value = "postId") Long postId);
}
