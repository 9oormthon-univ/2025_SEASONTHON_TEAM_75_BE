package com.trashheroesbe.feature.rank.application.job;

import com.trashheroesbe.feature.rank.domain.TrashRank;
import com.trashheroesbe.feature.rank.infrastructure.TrashRankRepository;
import com.trashheroesbe.feature.search.infrastructure.SearchLogRepository;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RankSchedulerService {

    private final TrashTypeRepository trashTypeRepository;
    private final SearchLogRepository searchLogRepository;
    private final TrashRankRepository trashRankRepository;

    public void updateTrashRankEvery3hours() {
        try {
            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusHours(3);

            List<Object[]> recentSearchResults = searchLogRepository.countSearchLogsByTrashTypeAndDateRange(
                startTime, endTime);

            Map<TrashType, Integer> recentSearchCounts = recentSearchResults.stream()
                .collect(Collectors.toMap(
                    result -> (TrashType) result[0],
                    result -> ((Long) result[1]).intValue()
                ));

            if (recentSearchCounts.isEmpty()) {
                return;
            }

            List<TrashType> allTypes = trashTypeRepository.findAll();
            Map<TrashType, Integer> searchCounts = allTypes.stream()
                .collect(Collectors.toMap(
                    type -> type,
                    type -> recentSearchCounts.getOrDefault(type, 0)
                ));

            List<TrashRank> currentRankings = trashRankRepository.findAllOrderByRankOrder();

            Map<TrashType, TrashRank> currentRankingMap = currentRankings.stream()
                .collect(Collectors.toMap(TrashRank::getTrashType, rank -> rank));

            // 더티 체크 or save 명시
            List<TrashRank> updateRankings = processCalculate(searchCounts, currentRankingMap);
            trashRankRepository.saveAll(updateRankings);

        } catch (Exception e) {
            log.error("스케줄러 : 3시간 랭킹 수집 실패", e);
        }
    }

    private List<TrashRank> processCalculate(
        Map<TrashType, Integer> searchCounts,
        Map<TrashType, TrashRank> currentRankingMap
    ) {
        List<TrashRank> updatedRankings = searchCounts.entrySet().stream()
            .map(entry -> {
                TrashType trashType = entry.getKey();
                Integer recentSearchCount = entry.getValue();

                TrashRank currentRank = currentRankingMap.get(trashType);
                if (currentRank == null) {
                    return null;
                }
                Integer searchCount = currentRank.getSearchCount();
                currentRank.updateTotalSearchCount(searchCount, recentSearchCount);
                return currentRank;

            })
            .filter(Objects::nonNull)
            .toList();

        updatedRankings.sort((a, b) -> b.getSearchCount().compareTo(a.getSearchCount()));

        return IntStream.range(0, updatedRankings.size())
            .mapToObj(i -> {
                TrashRank rank = updatedRankings.get(i);
                int newRankOrder = i + 1;

                rank.calculateTrendDirection(newRankOrder);
                rank.updateLastUpdated();

                return rank;
            })
            .toList();
    }
}