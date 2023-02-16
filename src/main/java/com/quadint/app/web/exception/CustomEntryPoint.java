package com.quadint.app.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadint.app.web.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class CustomEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String errorMessage;
        if (authException instanceof InsufficientAuthenticationException) {
            errorMessage = "token is null or invalid";
        }
        else {
            errorMessage = "customEntryPoint error";
        }
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getWriter(), Response.error(errorMessage));
    }
}
