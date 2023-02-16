package com.sparta.soomtut.service.impl;

import com.sparta.soomtut.dto.request.PostRequestDto;
import com.sparta.soomtut.dto.request.UpdatePostRequestDto;
import com.sparta.soomtut.dto.response.PostResponseDto;
import com.sparta.soomtut.entity.Member;
import com.sparta.soomtut.entity.Post;
import com.sparta.soomtut.enums.MemberRole;
import com.sparta.soomtut.exception.ErrorCode;
import com.sparta.soomtut.repository.PostRepository;

import com.sparta.soomtut.service.interfaces.PostService;
import com.sparta.soomtut.service.interfaces.LocationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LocationService locationService;


    // 게시글 작성
    @Transactional
    public PostResponseDto createPost(Member member, PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto, member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
        );

        // 작성자 또는 관리자만 수정가능
        if (member.getMemberRole() != MemberRole.ADMIN) {
            if (!post.getTutorId().equals(member.getId()))
                throw new IllegalArgumentException(ErrorCode.AUTHORIZATION.getMessage());
        }

        post.update(updatePostRequestDto);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_POST.getMessage())
        );

        if (member.getMemberRole() == MemberRole.ADMIN)
            postRepository.delete(post);

        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public Post findPostById(Long postId){
       Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CLASS.getMessage())
        );

        return post;
    }
    @Override
    @Transactional
    public Long getTutorId(Long postId) {

        return findPostById(postId).getTutorId();

    }

    @Override
    public PostResponseDto getMyPost(Member member) {
        Post post = postRepository.findByTutorId(member.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_CLASS.getMessage()));
        PostResponseDto postResponseDto = new PostResponseDto(post, member.getNickname(), locationService.findMemberLocation(member.getId()).getAddress());
        return postResponseDto;
    }
}
