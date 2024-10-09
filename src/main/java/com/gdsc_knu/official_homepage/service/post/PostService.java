package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
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

    @Scheduled(cron = "0 0 0 * * ?")
    public void invokeClearPost() {
        clearTrendingPosts();
    }

    @CacheEvict(value = "trending-post", allEntries = true, beforeInvocation = true)
    public void clearTrendingPosts() {
        log.info("trending post 초기화");
    }
}
