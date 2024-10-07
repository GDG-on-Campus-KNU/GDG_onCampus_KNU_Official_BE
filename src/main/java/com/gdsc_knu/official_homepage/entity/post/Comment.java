package com.gdsc_knu.official_homepage.entity.post;


import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

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

    public static Comment create(Post post, String content, Member member, Comment parent) {
        return Comment.builder()
                .post(post)
                .content(content)
                .author(member)
                .authorName(member.getName())
                .authorProfile(member.getProfileUrl())
                .parent(parent)
                .build();
    }


}
