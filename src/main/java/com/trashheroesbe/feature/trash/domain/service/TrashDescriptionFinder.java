package com.trashheroesbe.feature.trash.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_DESCRIPTION;

import com.trashheroesbe.feature.trash.domain.entity.TrashDescription;
import com.trashheroesbe.feature.trash.infrastructure.TrashDescriptionRepository;
import com.trashheroesbe.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashDescriptionFinder {

    private final TrashDescriptionRepository trashDescriptionRepository;

    public TrashDescription findTrashDescriptionsByTrashTypeId(Long trashTypeId) {
        return trashDescriptionRepository.findByTrashTypeId(trashTypeId)
            .orElseThrow(() -> new BusinessException(NOT_EXISTS_TRASH_DESCRIPTION));
    }
}
