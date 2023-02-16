package com.quadint.app.web.controller.response;

import lombok.Getter;

@Getter
public class LoginSuccessTokenResponse {
    private String access;
    private String refresh;

    public LoginSuccessTokenResponse(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }
}
