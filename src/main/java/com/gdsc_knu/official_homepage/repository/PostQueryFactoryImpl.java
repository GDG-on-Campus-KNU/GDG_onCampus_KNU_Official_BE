package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.QPost;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryFactoryImpl implements PostQueryFactory{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Post> findTop5ByCategory(Category category, int size) {
        return jpaQueryFactory
                .selectFrom(QPost.post)
                .where(QPost.post.status.eq(PostStatus.SAVED)
                        .and(eqCategory(category)))
                .orderBy(QPost.post.likeCount.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public Page<Post> findAllByCategory(Pageable pageable, Category category) {
        List<Post> postList = jpaQueryFactory
                .selectFrom(QPost.post)
                .where(QPost.post.status.eq(PostStatus.SAVED)
                        .and(eqCategory(category)))
                .fetch();
        return new PageImpl<>(postList, pageable, postList.size());
    }

    private BooleanExpression eqCategory(Category category) {
        return category == null ? null : QPost.post.category.eq(category);
    }
}
