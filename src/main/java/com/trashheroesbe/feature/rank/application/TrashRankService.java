package com.trashheroesbe.feature.rank.application;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import com.trashheroesbe.feature.rank.dto.response.TrashRankResponse;
import com.trashheroesbe.feature.rank.dto.response.WeeklyRankResponse;
import com.trashheroesbe.feature.rank.infrastructure.TrashRankRepository;
import com.trashheroesbe.global.util.DateTimeUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrashRankService {

    private final TrashRankRepository trashRankRepository;

    public WeeklyRankResponse getCurrentWeekRanking() {
        LocalDate currentWeekStart = DateTimeUtils.getCurrentWeekStart();
        LocalDate currentWeekEnd = DateTimeUtils.getCurrentWeekEnd();

        List<TrashRank> rankings = trashRankRepository.findCurrentWeekRanking(currentWeekStart);

        if (rankings.isEmpty()) {
            return WeeklyRankResponse.of(currentWeekStart, currentWeekEnd, List.of());
        }

        List<TrashRankResponse> rankingResponses = rankings.stream()
            .map(TrashRankResponse::from)
            .collect(Collectors.toList());

        return WeeklyRankResponse.of(currentWeekStart, currentWeekEnd, rankingResponses);
    }
}
