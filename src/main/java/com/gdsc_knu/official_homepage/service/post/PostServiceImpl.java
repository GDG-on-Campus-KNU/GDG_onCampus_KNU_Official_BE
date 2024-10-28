package com.gdsc_knu.official_homepage.service.post;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.post.AccessModel;
import com.gdsc_knu.official_homepage.dto.post.PostRequest;
import com.gdsc_knu.official_homepage.dto.post.PostResponse;
import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import com.gdsc_knu.official_homepage.entity.post.Post;
import com.gdsc_knu.official_homepage.entity.post.PostLike;
import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.member.MemberRepository;
import com.gdsc_knu.official_homepage.repository.post.PostLikeRepository;
import com.gdsc_knu.official_homepage.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 게시글 작성, 회원만 작성 가능
     * @param memberId 회원 id
     * @param postRequest 게시글 작성 요청 DTO
     * @throws CustomException ErrorCode.USER_NOT_FOUND
     */
    @Override
    @Transactional
    public void createPost(Long memberId, PostRequest.Create postRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = PostRequest.Create.toEntity(postRequest, member);
        postRepository.save(post);
    }

    /**
     * 게시글 조회, 저장(SAVED)된 게시글만 조회 가능
     * @param postId 게시글 id
     * @return PostResponse.Detail
     * @throws CustomException ErrorCode.POST_NOT_FOUND
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponse.Detail getPost(Long memberId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.isSaved()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        AccessModel access = AccessModel.calcPostAccess(memberId, post.getMember());
        boolean isLiked = memberId != 0L && postLikeRepository.findByMemberIdAndPostId(memberId, postId).isPresent();
        return PostResponse.Detail.from(post, access, isLiked);
    }

    /**
     * 카테고리별 게시글 목록 조회, 저장(SAVED)된 게시글만 조회 가능
     * @param category 카테고리, null이면 전체 조회
     * @return List<PostResponse.Main>
     */
    @Override
    @Transactional(readOnly = true)
    public PagingResponse<PostResponse.Main> getPostList(Category category, int page, int size) {
        Page<Post> postPage = postRepository.findAllByCategory(PageRequest.of(page, size), category);
        return PagingResponse.withoutCountFrom(postPage, size, PostResponse.Main::from);
    }

    @Override
    @Transactional(readOnly = true)
    public PagingResponse<PostResponse.Temp> getTemporalPostList(Long memberId, PostStatus status, int page, int size) {
        Page<Post> postList = postRepository.findAllByMemberIdAndStatus(memberId, status, PageRequest.of(page, size));
        return PagingResponse.withoutCountFrom(postList, size, PostResponse.Temp::from);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public PostResponse.Modify getModifyPost(Long memberId, Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
//        if (!post.getMember().getId().equals(memberId)) {
//            throw new CustomException(ErrorCode.POST_FORBIDDEN);
//        }
//        PostResponse.Modify modifyPost = PostResponse.Modify.from(post);
//
//        return modifyPost;
//    }

    @Override
    @Transactional(readOnly = true)
    public PagingResponse<PostResponse.Main> searchPostList(String keyword, int page, int size) {
        Page<Post> postPage = postRepository.searchByKeyword(PageRequest.of(page, size), keyword);
        return PagingResponse.withoutCountFrom(postPage, size, PostResponse.Main::from);
    }

    /**
     * 게시글 수정, 작성자만 수정 가능
     * @param memberId 회원 id
     * @param postId 게시글 id
     * @param postRequest 게시글 수정 요청 DTO
     * @throws CustomException ErrorCode.POST_NOT_FOUND, ErrorCode.POST_FORBIDDEN
     */
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

    /**
     * 게시글 삭제, 작성자 혹은 관리자(Core)만 삭제 가능
     * @param memberId 회원 id
     * @param postId 게시글 id
     * @throws CustomException ErrorCode.POST_NOT_FOUND, ErrorCode.USER_NOT_FOUND, ErrorCode.POST_FORBIDDEN
     */
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

    @Override
    public void likePost(Long memberId, Long postId) {
        postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .ifPresent(postLike -> {
                    throw new CustomException(ErrorCode.POST_ALREADY_LIKED);
                });
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        postLikeRepository.save(PostLike.from(post, member));
        post.addLikeCount();
    }

    @Override
    public void unlikePost(Long memberId, Long postId) {
        PostLike postLike = postLikeRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_LIKED));
        postLikeRepository.delete(postLike);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.subtractLikeCount();
    }

    @Override
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
