package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query(value = "select exists(select 1 " +
            "from post_like " +
            "where member_id = :memberId " +
            "and post_id = :postId)", nativeQuery = true)
    int existsByMemberAndPost(Long memberId, Long postId);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
