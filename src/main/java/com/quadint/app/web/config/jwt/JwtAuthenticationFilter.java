package com.quadint.app.web.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadint.app.domain.User;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.exception.TtoAppException;
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

    private final AuthenticationManager authenticationManager;
    private final String key;
    private final Long expiredTimeMs;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(token);
            return authenticate;
        } catch (IOException e) {
            throw new TtoAppException("attemptAuthentication error");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = JwtTokenUtils.generateToken(user.getUsername(), key, expiredTimeMs);
        response.setContentType("application/json");
        response.setHeader("Authorization", "Bearer " + token);

        new ObjectMapper().writeValue(response.getWriter(), Response.success(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
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
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getWriter(), Response.error(errorMessage));
    }
}
