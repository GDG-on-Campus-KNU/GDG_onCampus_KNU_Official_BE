package com.gdsc_knu.official_homepage.entity.post;


import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
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
    @JoinColumn(nullable = false)
    private Post post;

    @Column(length = 4000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    // 작성자 비정규화
    private String authorName;
    @Column(length = 500)
    private String authorProfile;
    private Long authorId;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    public static Comment from(String content, Member author, Post post, Comment parent) {
        Comment comment = Comment.builder()
                .post(post)
                .content(content)
                .author(author)
                .authorName(author.getName())
                .authorProfile(author.getProfileUrl())
                .authorId(author.getId())
                .build();
        if (parent == null) {
            comment.parent = comment;
        }
        else {
            comment.parent = parent;
            parent.replies.add(comment);
        }
        post.commentList.add(comment);
        post.addCommentCount();
        return comment;
    }

    public void update(String content, long memberId) {
        if (this.authorId != memberId) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
        this.content = content;
    }

    public boolean isChild(){
        // 프로퍼티 접근
        return !this.parent.getId().equals(this.id);
    }

    public boolean isCommentAuthor(Long memberId) {
        return this.authorId.equals(memberId);
    }


    public void delete(long memberId) {
        if (this.authorId != memberId) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
        this.post.commentList.remove(this);
        int deleteCount = 1 + this.getReplies().size();
        this.post.subtractCommentCount(deleteCount);
    }


}
