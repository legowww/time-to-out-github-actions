package com.quadint.app.web.repository;

import com.quadint.app.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenEntityRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByUserId(Integer userId);
    Optional<TokenEntity> findByToken(String token);

    int deleteByToken(String token);
}
