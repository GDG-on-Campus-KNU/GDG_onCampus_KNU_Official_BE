package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryFactory {
    List<Post> findAllByMemberIdAndStatus(Long memberId, PostStatus status);
}
