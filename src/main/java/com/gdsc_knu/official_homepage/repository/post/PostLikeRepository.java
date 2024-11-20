package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
