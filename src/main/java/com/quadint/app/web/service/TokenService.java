package com.quadint.app.web.service;


import com.quadint.app.domain.User;
import com.quadint.app.domain.UserRole;
import com.quadint.app.domain.entity.TokenEntity;
import com.quadint.app.web.exception.TtoAppException;
import com.quadint.app.web.repository.TokenEntityRepository;
import com.quadint.app.web.util.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenEntityRepository tokenEntityRepository;

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.access-token-expired-time-ms}")
    private Long accessTokenExpiredTimeMs;

    @Transactional
    public void save(Integer userId, String token) {
        Optional<TokenEntity> optionalTokenEntity = tokenEntityRepository.findByUserId(userId);

        if (optionalTokenEntity.isPresent()) {
            TokenEntity tokenEntity = optionalTokenEntity.get();
            //refresh token update
            tokenEntity.setToken(token);
        }
        else {
            //refresh token save
            tokenEntityRepository.save(new TokenEntity(userId, token));
        }
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        if (tokenEntityRepository.deleteByToken(refreshToken) == 0) {
            throw new TtoAppException("Refresh token does not exist.");
        }
    }

    @Transactional(noRollbackFor = TtoAppException.class) //ROLLBACK 하지 않도록 함
    public String reIssueAccessToken(String refreshToken) {
        Optional<TokenEntity> optionalTokenEntity = tokenEntityRepository.findByToken(refreshToken);

        //refresh token is valid
        if (optionalTokenEntity.isPresent()) {
            try {
                JwtTokenUtils.isExpired(refreshToken, key);
            } catch (ExpiredJwtException e) {
                log.info("=====");
                TokenEntity tokenEntity = optionalTokenEntity.get();
                int i = tokenEntityRepository.deleteByToken(refreshToken);
                log.info("id={}, token={}, result={}", tokenEntity.getId(), tokenEntity.getToken(), i); //예외 발생시 ROLLBACK 발생!
                throw new TtoAppException("Refresh token has expired."); //여기서 TtoAppException 발생, 하지만 noRollbackFor 64번째 코드까지는 commit() 된다.
            }

            TokenEntity tokenEntity = optionalTokenEntity.get();
            log.info("id={} 의 액세스 토큰 재발급", tokenEntity.getUserId());
            String accessToken = JwtTokenUtils.generateAccessToken(tokenEntity.getUserId(), UserRole.ROLE_USER.toString(), key, accessTokenExpiredTimeMs);
            return accessToken;
        }
        else {
            throw new TtoAppException("Refresh token does not exist.");
        }
    }

    public void checkValidAuthentication(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
        }
        catch (RuntimeException e) {
            throw new TtoAppException("checkValidAuthentication exception");
        }
    }

}
