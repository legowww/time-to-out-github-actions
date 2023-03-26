package com.quadint.app.web.config.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.util.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {
    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
            }
            else {
                final String token = header.split(" ")[1].trim();
                JwtTokenUtils.isExpired(token, key);
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getWriter(), Response.error("Access token expires, refresh token needs to be sent"));
        } catch (JwtException e) {
            log.error("JwtException");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getWriter(), Response.error("Invalid token format."));
        }
    }
}
