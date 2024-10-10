package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.MemberRepository;
import com.gdsc_knu.official_homepage.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createPost(Long memberId, PostRequest.Create postRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = new Post(postRequest, member);
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse.Main getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.isSaved()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return PostResponse.Main.from(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse.Main> getPostList(Category category) {
        List<Post> postList;
        if (category != null) {
            postList = postRepository.findAllByCategory(category);
        }
        else {
            postList = postRepository.findAll();
        }
        return postList.stream()
                .filter(Post::isSaved)
                .map(PostResponse.Main::from)
                .toList();
    }

    @Override
    @Transactional
    public void updatePost(Long memberId, Long postId, PostRequest.Update postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }
        post.update(postRequest);
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!post.getMember().getId().equals(memberId) && !member.getRole().equals(Role.ROLE_CORE)) {
            // 코어는 게시글 삭제도 가능
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }
        postRepository.delete(post);
    }
}
