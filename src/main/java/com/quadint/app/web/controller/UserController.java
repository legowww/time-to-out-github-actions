package com.quadint.app.web.controller;


import com.quadint.app.domain.User;
import com.quadint.app.web.controller.request.UserJoinRequest;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public Response<String> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUsername(), request.getPassword());
        return Response.success(user.toString());
    }

    @GetMapping("/test")
    public Response<String> test(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Response.success(user.toString());
    }
}
