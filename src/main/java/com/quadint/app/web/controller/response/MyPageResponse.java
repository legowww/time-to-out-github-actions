package com.quadint.app.web.controller.response;

import com.quadint.app.domain.entity.UserEntity;
import lombok.Getter;

@Getter
public class MyPageResponse {
    private String name;
    private String username;

    private MyPageResponse(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public static MyPageResponse fromEntity(UserEntity entity) {
        return new MyPageResponse(
                entity.getName(),
                entity.getUsername()
        );
    }
}
