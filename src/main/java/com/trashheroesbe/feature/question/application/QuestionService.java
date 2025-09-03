package com.trashheroesbe.feature.question.application;

import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_ITEM;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_TYPE;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.domain.service.TrashItemFinder;
import com.trashheroesbe.feature.trash.domain.service.TrashTypeFinder;
import com.trashheroesbe.feature.trash.dto.response.TrashItemResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashTypeResponse;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final TrashTypeFinder trashTypeFinder;
    private final TrashItemFinder trashItemFinder;

    public List<TrashTypeResponse> getTrashTypes() {
        List<TrashType> trashTypes = trashTypeFinder.getAllTrashTypes();
        if (trashTypes.isEmpty()) {
            throw new BusinessException(NOT_EXISTS_TRASH_TYPE);
        }
        return trashTypes.stream()
            .map(TrashTypeResponse::from)
            .collect(Collectors.toList());
    }

    public List<TrashItemResponse> getTrashItems(Long trashTypeId) {
        List<TrashItem> trashItems = trashItemFinder.findTrashItemsByTrashTypeId(trashTypeId);
        if (trashItems.isEmpty()) {
            throw new BusinessException(NOT_EXISTS_TRASH_ITEM);
        }
        return trashItems.stream()
            .map(TrashItemResponse::from)
            .collect(Collectors.toList());
    }
}
