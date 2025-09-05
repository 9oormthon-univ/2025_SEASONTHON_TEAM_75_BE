package com.trashheroesbe.feature.rank.application;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import com.trashheroesbe.feature.rank.dto.response.TrashRankResponse;
import com.trashheroesbe.feature.rank.dto.response.RankResponse;
import com.trashheroesbe.feature.rank.infrastructure.TrashRankRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrashRankService {

    private final TrashRankRepository trashRankRepository;

    public RankResponse getCurrentWeekRanking() {
        List<TrashRank> rankings = trashRankRepository.findAllOrderByRankOrder();

        if (rankings.isEmpty()) {
            return RankResponse.of(List.of(), LocalDateTime.now());
        }

        List<TrashRankResponse> rankingResponses = rankings.stream()
            .map(TrashRankResponse::from)
            .collect(Collectors.toList());

        LocalDateTime lastUpdated = rankings.stream()
            .map(TrashRank::getLastUpdated)
            .max(LocalDateTime::compareTo)
            .orElse(LocalDateTime.now());

        return RankResponse.of(rankingResponses, lastUpdated);
    }
}
