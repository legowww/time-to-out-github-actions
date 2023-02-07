package com.quadint.app.web.service;


import com.quadint.app.domain.Favorite;
import com.quadint.app.domain.entity.FavoriteEntity;
import com.quadint.app.domain.entity.LocationCoordinate;
import com.quadint.app.domain.entity.UserEntity;
import com.quadint.app.web.controller.request.FavoriteLocationCoordinateRequest;
import com.quadint.app.web.exception.TtoAppException;
import com.quadint.app.web.repository.FavoriteEntityRepository;
import com.quadint.app.web.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {
    private final UserEntityRepository userEntityRepository;
    private final FavoriteEntityRepository favoriteEntityRepository;

    @Transactional
    public void add(Integer userId, FavoriteLocationCoordinateRequest request) {
        if (favoriteEntityRepository.countAllByUserEntity_Id(userId) >= 5) {
            throw new TtoAppException("You cannot create more than 5 favorites.");
        }

        UserEntity userEntity = userEntityRepository.getOne(userId);
        favoriteEntityRepository.save(FavoriteEntity.of(userEntity, request.getName(), LocationCoordinate.fromRequest(request)));
    }

    @Transactional
    public void delete(Integer favoriteId) {
        favoriteEntityRepository.deleteById(favoriteId);
    }

    public List<Favorite> favorites(Integer userId, Pageable pageable) {
        return favoriteEntityRepository.findAllByUserEntity_Id(userId, pageable).map(Favorite::new).getContent();
    }
}
