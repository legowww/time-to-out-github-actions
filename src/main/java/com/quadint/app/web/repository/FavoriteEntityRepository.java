package com.quadint.app.web.repository;

import com.quadint.app.domain.entity.FavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteEntityRepository extends JpaRepository<FavoriteEntity, Integer> {
    Page<FavoriteEntity> findAllByUserEntity_Id(Integer userId, Pageable pageable);
    int countAllByUserEntity_Id(Integer integer);
}
