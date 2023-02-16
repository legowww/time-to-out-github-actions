package com.quadint.app.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtTokenUtils {
    public static Integer getId(String token, String key) {
        return extractClaims(token, key).get("userId", Integer.class);
    }

    public static String getUserRole(String token, String key) {
        return extractClaims(token, key).get("role", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration(); //여기서 ExpiredJwtException 발생? -> 정확하게는 extractClaims
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(key))
                .build()
                .parseClaimsJws(token).getBody();//여기서 ExpiredJwtException 발생
    }


    public static String generateAccessToken(Integer userId, String role, String key, long expiredTimeMs) {
        Date date = new Date(System.currentTimeMillis());
        log.info("access token 발급 시간={}", date);
        log.info("access token 만료 시간={}", new Date(System.currentTimeMillis() + expiredTimeMs));

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken(String key, long expiredTimeMs) {
        Date date = new Date(System.currentTimeMillis());
        log.info("refresh token 발급 시간={}", date);
        log.info("refresh token 만료 시간={}", new Date(System.currentTimeMillis() + expiredTimeMs));

        Claims claims = Jwts.claims();
        claims.put("uuid", UUID.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }


    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
