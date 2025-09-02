package com.trashheroesbe.feature.user.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
    }

}
