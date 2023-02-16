package com.quadint.app.web.service;

import com.quadint.app.domain.User;
import com.quadint.app.domain.entity.UserEntity;
import com.quadint.app.web.controller.response.MyPageResponse;
import com.quadint.app.web.exception.TtoAppException;
import com.quadint.app.web.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public User join(String name, String username, String password) {
        if (userEntityRepository.existsByUsername(username)) {
            throw new TtoAppException("This id already exists");
        }

        String encodePassword = encoder.encode(password);
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(name, username, encodePassword));
        return User.fromEntity(userEntity);
    }

    public MyPageResponse myPage(Integer userId) {
        return userEntityRepository.findById(userId)
                .map(MyPageResponse::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("unfounded userId"));
    }
}
