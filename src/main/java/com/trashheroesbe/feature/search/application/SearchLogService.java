package com.trashheroesbe.feature.search.application;

import com.trashheroesbe.feature.search.domain.LogSource;
import com.trashheroesbe.feature.search.domain.SearchLog;
import com.trashheroesbe.feature.search.infrastructure.SearchLogRepository;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    @Transactional
    public void log(LogSource logSource, TrashType trashType, User user) {
        SearchLog searchLog = SearchLog.create(logSource, trashType, user);
        searchLogRepository.save(searchLog);
    }
}
