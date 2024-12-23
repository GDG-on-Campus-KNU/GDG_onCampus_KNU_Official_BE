package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryFactory {
    List<Post> findAllByMemberIdAndStatus(Long memberId, PostStatus status, Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p JOIN FETCH p.member " +
            "WHERE p.id=:id")
    Optional<Post> findByIdWithAuthor(Long id);
}
