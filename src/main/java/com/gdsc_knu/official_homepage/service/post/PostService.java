package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;

import java.util.List;

public interface PostService {
    void createPost(Long memberId, PostRequest.Create postRequest);

    PostResponse.Detail getPost(Long memberId, Long postId);

    PagingResponse<PostResponse.Main> getPostList(Category category, int page, int size);

    PagingResponse<PostResponse.Temp> getTemporalPostList(Long memberId, PostStatus status, int page, int size);

//    PostResponse.Modify getModifyPost(Long memberId, Long postId);

    void updatePost(Long memberId, Long postId, PostRequest.Update postRequest);

    void deletePost(Long memberId, Long postId);

    PagingResponse<PostResponse.Main> searchPostList(String keyword, int page, int size);
  
    List<PostResponse.Main> getTrendingPosts(Category category, int size);
}
