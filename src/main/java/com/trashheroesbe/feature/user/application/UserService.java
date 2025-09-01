package com.trashheroesbe.feature.user.application;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.domain.service.UserFinder;
import com.trashheroesbe.feature.user.dto.request.UpdateUserRequest;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserFinder userFinder;
    private final UserRepository userRepository;

    @Transactional
    public void updateUser(UpdateUserRequest request, Long userId) {
        User user = userFinder.findById(userId);

        if (request.nickname() != null) {
            user.updateNickname(request.nickname());
        }



    }
}
