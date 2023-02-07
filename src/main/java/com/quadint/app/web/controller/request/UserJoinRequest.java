package com.quadint.app.web.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserJoinRequest {
    @NotBlank(message = "Please enter a valid name")
    private String name;
    @NotBlank(message = "Please enter a valid id")
    private String username;
    @NotBlank(message = "Please enter a valid password")
    private String password;
}
