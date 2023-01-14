package com.quadint.app.web.controller;


import com.quadint.app.domain.User;
import com.quadint.app.web.controller.request.UserJoinRequest;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public Response<String> join(@RequestBody UserJoinRequest request) {
        User joinedUser = userService.join(request.getUsername(), request.getPassword());
        return Response.success(joinedUser.toString());
    }
}
