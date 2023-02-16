package com.quadint.app.web.config.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadint.app.domain.User;
import com.quadint.app.web.controller.response.LoginSuccessTokenResponse;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.service.TokenService;
import com.quadint.app.web.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final String key;
    private final Long accessTokenExpiredTimeMs;
    private final Long refreshTokenExpiredTimeMs;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(token);
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String accessToken = JwtTokenUtils.generateAccessToken(user.getId(), user.getRole().toString(), key, accessTokenExpiredTimeMs);
        String refreshToken = JwtTokenUtils.generateRefreshToken(key, refreshTokenExpiredTimeMs);

        //refresh token save
        tokenService.save(user.getId(), refreshToken);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), Response.success(new LoginSuccessTokenResponse(accessToken, refreshToken)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String errorMessage;
        if (failed instanceof UsernameNotFoundException) {
            errorMessage = "unfounded username";
        }
        else if (failed instanceof BadCredentialsException) {
            errorMessage = "wrong password";
        }
        else {
            errorMessage = "unidentified Error";
        }

        new ObjectMapper().writeValue(response.getWriter(), Response.error(errorMessage));
    }
}
