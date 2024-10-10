package com.gdsc_knu.official_homepage.entity.post;

import com.gdsc_knu.official_homepage.dto.post.PostRequest;
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

    private String subTitle;

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

    public void update(PostRequest.Update postRequest) {
        this.title = postRequest.getTitle();
        this.subTitle = postRequest.getSubTitle();
        this.content = postRequest.getContent();
        this.thumbnailUrl = postRequest.getThumbnailUrl();
        this.category = postRequest.getCategory();
        this.status = postRequest.getStatus();
        this.modifiedAt = LocalDateTime.now();
    }

    public Post(PostRequest.Create postRequest, Member member) {
        this.title = postRequest.getTitle();
        this.subTitle = postRequest.getSubTitle();
        this.content = postRequest.getContent();
        this.member = member;
        this.thumbnailUrl = postRequest.getThumbnailUrl();
        this.category = postRequest.getCategory();
        this.status = postRequest.getStatus();
        this.publishedAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean isSaved() {
        return this.status.equals(PostStatus.SAVED);
    }
}
