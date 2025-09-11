package com.trashheroesbe.feature.user.job;

import com.trashheroesbe.feature.user.application.job.UserSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestUserCleanupJob {

    private final UserSchedulerService userSchedulerService;

    @Scheduled(cron = "0 5 0 * * *")
    public void runGuestUserCleanupJob() {
        userSchedulerService.deleteGuestUser();
    }
}
