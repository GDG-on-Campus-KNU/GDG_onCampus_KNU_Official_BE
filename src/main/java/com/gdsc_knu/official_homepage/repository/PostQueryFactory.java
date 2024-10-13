package com.gdsc_knu.official_homepage.repository;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;

import java.util.List;

public interface PostQueryFactory {
    List<Post> findTop5ByCategory(Category category, int size);

    List<Post> findAllByCategory(Category category);
}
