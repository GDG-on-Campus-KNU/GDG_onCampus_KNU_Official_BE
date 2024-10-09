package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createPost(Long memberId, PostRequest.Create postRequestDto) {

    }

    @Override
    public PostResponse getPost(Long postId) {
        return null;
    }

    @Override
    public List<PostResponse> getPostList(Category category) {
        return null;
    }

    @Override
    public void updatePost(Long memberId, Long postId, PostRequest.Update postRequestDto) {

    }

    @Override
    public void deletePost(Long memberId, Long postId) {

    }
}
