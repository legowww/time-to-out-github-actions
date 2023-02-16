package com.quadint.app.web.controller;


import com.quadint.app.domain.Favorite;
import com.quadint.app.domain.User;
import com.quadint.app.web.controller.request.FavoriteLocationCoordinateRequest;
import com.quadint.app.web.controller.request.UserJoinRequest;
import com.quadint.app.web.controller.response.MyPageResponse;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.service.FavoriteService;
import com.quadint.app.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    @PostMapping("/join")
    public Response<Void> join(@Validated @RequestBody UserJoinRequest request) {
        userService.join(request.getName(), request.getUsername(), request.getPassword());
        return Response.success();
    }

    @GetMapping("/my-page")
    public Response myPage(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MyPageResponse myPage = userService.myPage(user.getId());
        return Response.success(myPage);
    }

    @GetMapping("/favorites")
    public Response<List<Favorite>> favorites(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Favorite> favorites = favoriteService.favorites(user.getId(), pageable);
        return Response.success(favorites);
    }

    @PostMapping("/favorites")
    public Response<Void> add(@RequestBody FavoriteLocationCoordinateRequest request,
                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        favoriteService.add(user.getId(), request);
        return Response.success();
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public Response<Void> delete(@PathVariable Integer favoriteId) {
        favoriteService.delete(favoriteId);
        return Response.success();
    }
}
