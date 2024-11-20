package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryFactory {
    Page<Post> findAllByMemberIdAndStatus(Long memberId, PostStatus status, Pageable pageable);
}
