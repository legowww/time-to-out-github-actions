package com.quadint.app.web.service;

import com.quadint.app.domain.User;
import com.quadint.app.domain.entity.UserEntity;
import com.quadint.app.web.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public User join(String username, String password) {
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, password));
        return User.fromEntity(userEntity);
    }
}
