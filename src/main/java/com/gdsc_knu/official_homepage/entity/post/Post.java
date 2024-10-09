package com.gdsc_knu.official_homepage.entity.post;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> commentList = new ArrayList<>();

    private String title;

    @Column(length = 65535)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private int likeCount;
    private int commentCount;
    private int sharedCount;

    private LocalDateTime publishedAt;
    private LocalDateTime modifiedAt;

}
