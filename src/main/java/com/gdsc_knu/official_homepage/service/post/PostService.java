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

public interface PostService {
    void createPost(Long memberId, PostRequest.Create postRequest);

    PostResponse.Main getPost(Long postId);

    List<PostResponse.Main> getPostList(Category category);

    void updatePost(Long memberId, Long postId, PostRequest.Update postRequest);

    void deletePost(Long memberId, Long postId);
  
    List<PostResponse.Main> getTrendingPosts(Category category, int size);
}
