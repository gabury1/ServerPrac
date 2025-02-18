package com.backend.ServerPrac.domain.user;

import com.backend.ServerPrac.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor @Slf4j
public class UserController 
{
    private final UserService userService;

    @Value("${test.testStr}")
    private String testStr;

    @GetMapping("/")
    public String test()
    {
        return testStr + "success! ";
    }

    @PostMapping("/user")
    public ApiResponse<String> signup()
    {
        String userNo = userService.submit().getId().toString();
        log.info("New USER : " + userNo);
        return ApiResponse.onSuccess(userNo);
    }


}
