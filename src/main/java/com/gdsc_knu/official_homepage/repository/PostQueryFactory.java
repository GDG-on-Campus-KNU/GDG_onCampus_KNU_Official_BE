package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryFactory {
    List<Post> findTop5ByCategory(Category category, int size);

    Page<Post> findAllByCategory(Pageable pageable, Category category);
}
