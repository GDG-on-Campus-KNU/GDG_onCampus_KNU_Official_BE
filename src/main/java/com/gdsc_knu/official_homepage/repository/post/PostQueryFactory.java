package com.gdsc_knu.official_homepage.repository.post;

import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryFactory {
    List<Post> findTop5ByCategory(Category category, int size);

    List<Post> findAllByCategory(Pageable pageable, Category category);

    List<Post> searchByKeyword(Pageable pageable, String keyword);
}
