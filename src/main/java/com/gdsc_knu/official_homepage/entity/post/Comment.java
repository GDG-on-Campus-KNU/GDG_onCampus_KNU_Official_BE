package com.gdsc_knu.official_homepage.entity.post;


import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(length = 4000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    // 작성자 비정규화
    private String authorName;
    @Column(length = 500)
    private String authorProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> replies = new ArrayList<>();

    public Comment(Post post, String content, Member author, Comment parent) {
        this.post = post;
        this.content = content;
        this.author = author;
        this.authorName = author.getName();
        this.authorProfile = author.getProfileUrl();
        this.parent = parent != null
                ? parent
                : this;
    }

    public void update(String content) {
        this.content = content;
    }


}
