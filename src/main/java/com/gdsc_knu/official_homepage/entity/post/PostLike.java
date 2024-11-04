package com.gdsc_knu.official_homepage.entity.post;

import com.gdsc_knu.official_homepage.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    public static PostLike from(Post post, Member member) {
        post.addLikeCount();
        return PostLike.builder()
                .post(post)
                .member(member)
                .build();
    }
}
