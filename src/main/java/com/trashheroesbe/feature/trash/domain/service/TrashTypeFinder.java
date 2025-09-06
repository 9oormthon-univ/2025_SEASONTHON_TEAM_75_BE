package com.trashheroesbe.feature.trash.domain.service;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_TYPE;

import com.trashheroesbe.feature.trash.domain.entity.Trash;
import com.trashheroesbe.feature.trash.domain.type.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.infrastructure.TrashTypeRepository;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashTypeFinder {

    private final TrashTypeRepository trashTypeRepository;

    public List<TrashType> getAllTrashTypes() {
        List<TrashType> trashTypes = trashTypeRepository.findAllByOrderByIdAsc();
        if (trashTypes.isEmpty()) {
            throw new BusinessException(NOT_EXISTS_TRASH_TYPE);
        }
        return trashTypes;
    }

    public TrashType getTrashType(Type type) {
        return trashTypeRepository.findByType(type)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
    }

    public TrashType getTrashTypeById(Long trashTypeId) {
        return trashTypeRepository.findById(trashTypeId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
    }

    public List<Type> getAllTypes() {
        return trashTypeRepository.findAllTypes();
    }
}
