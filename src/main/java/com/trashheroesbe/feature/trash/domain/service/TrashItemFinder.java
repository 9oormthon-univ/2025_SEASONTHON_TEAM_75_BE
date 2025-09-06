package com.trashheroesbe.feature.trash.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_ITEM;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.infrastructure.TrashItemRepository;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashItemFinder {

    private final TrashItemRepository trashItemRepository;

    public List<TrashItem> findTrashItemsByTrashTypeId(Long trashTypeId) {
        List<TrashItem> trashItems = trashItemRepository.findByTrashTypeId(trashTypeId);
        if (trashItems.isEmpty()) {
            throw new BusinessException(NOT_EXISTS_TRASH_ITEM);
        }
        return trashItems;
    }

    public List<String> getTrashItemNames() {
        return trashItemRepository.findAll().stream()
            .map(TrashItem::getName)
            .toList();
    }

    public TrashItem getTrashItemByName(String itemName) {
        return trashItemRepository.findByName(itemName);
    }
}
