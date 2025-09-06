package com.trashheroesbe.feature.rank.job;

import com.trashheroesbe.feature.rank.application.job.RankSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankUpdateJob {

    private final RankSchedulerService rankSchedulerService;

    @Scheduled(cron = "0 0 */3 * * *")
    public void runRankUpdateJob() {
        rankSchedulerService.updateTrashRankEvery3hours();
    }
}
