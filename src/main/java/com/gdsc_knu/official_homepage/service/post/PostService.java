package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "trending-post", key = "'Is '+#category", unless="#result.size()<5")
    public List<PostResponse.Main> getTrendingPosts(Category category, int size) {
        List<Post> posts = postRepository.findTop5ByCategory(category, size);
        return posts.stream().map(PostResponse.Main::from).toList();
    }
}
