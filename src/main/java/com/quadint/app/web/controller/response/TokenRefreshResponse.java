package com.quadint.app.web.controller.response;

import lombok.Getter;

@Getter
public class TokenRefreshResponse {
    private String access;

    public TokenRefreshResponse(String access) {
        this.access = access;
    }
}
