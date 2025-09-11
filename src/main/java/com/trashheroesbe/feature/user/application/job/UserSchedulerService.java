package com.trashheroesbe.feature.user.application.job;

import com.trashheroesbe.feature.user.domain.type.AuthProvider;
import com.trashheroesbe.feature.user.domain.type.Role;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserSchedulerService {

    private final UserRepository userRepository;

    public void deleteGuestUser() {
        try {
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            int deletedCount = userRepository.deleteExpiredGuestUsers(
                AuthProvider.GUEST, Role.GUEST, oneHourAgo);

            log.info("게스트 유저 {} 건 삭제 완료 (기준 시간: {})", deletedCount, oneHourAgo);

        } catch (Exception e) {
            log.error("게스트 유저 삭제 중 오류 발생", e);
            throw e;
        }
    }
}
