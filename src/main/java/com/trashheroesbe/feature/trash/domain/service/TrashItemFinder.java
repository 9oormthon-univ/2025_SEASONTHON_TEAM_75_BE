package com.trashheroesbe.feature.trash.domain.service;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.infrastructure.TrashItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashItemFinder {

    private final TrashItemRepository trashItemRepository;

    public List<TrashItem> findTrashItemsByTrashTypeId(Long trashTypeId) {
        return trashItemRepository.findByTrashTypeId(trashTypeId);
    }
}
