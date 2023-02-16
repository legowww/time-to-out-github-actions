package com.quadint.app.web.controller;

import com.quadint.app.web.controller.request.TokenRefreshRequest;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.controller.response.TokenRefreshResponse;
import com.quadint.app.web.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public Response<TokenRefreshResponse> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefresh();
        String accessToken = tokenService.reIssueAccessToken(refreshToken);
        return Response.success(new TokenRefreshResponse(accessToken));
    }

    @GetMapping("/auto-login")
    public Response autoLogin(Authentication authentication) {
        tokenService.checkValidAuthentication(authentication);
        return Response.success();
    }

    @PostMapping("/logout")
    public Response logout(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefresh();
        tokenService.deleteRefreshToken(refreshToken);
        return Response.success();
    }
}
