package com.backend.ServerPrac.domain.post;

import com.backend.ServerPrac.domain.post.dto.PostCreateDto;
import com.backend.ServerPrac.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController
{
    private final PostService postService;

    @PostMapping("/post")
    public ApiResponse<?> createPost(PostCreateDto postCreateDto)
    {
        Post p = postService.submit(postCreateDto);

        return ApiResponse.onSuccess(p);
    }

    @GetMapping("/exception")
    public ApiResponse<?> exceptionTest(@RequestParam("flag") Integer flag)
    {
        int result = postService.flagCheck(flag);

        return ApiResponse.onSuccess(result);
    }

}
