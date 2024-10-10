package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;

import java.util.List;

public interface PostService {
    void createPost(Long memberId, PostRequest.Create postRequest);

    PostResponse.Detail getPost(Long postId);

    List<PostResponse.Main> getPostList(Category category);

    List<PostResponse.Temp> getTemporalPostList(Long memberId);

    void updatePost(Long memberId, Long postId, PostRequest.Update postRequest);

    void deletePost(Long memberId, Long postId);
  
    List<PostResponse.Main> getTrendingPosts(Category category, int size);
}
