package com.trashheroesbe.feature.question.application;

import static com.trashheroesbe.feature.search.domain.LogSource.QUESTION;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_DESCRIPTION;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_ITEM;
import static com.trashheroesbe.global.response.type.ErrorCode.NOT_EXISTS_TRASH_TYPE;

import com.trashheroesbe.feature.search.application.SearchLogService;
import com.trashheroesbe.feature.trash.domain.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashDescription;
import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.domain.service.TrashDescriptionFinder;
import com.trashheroesbe.feature.trash.domain.service.TrashItemFinder;
import com.trashheroesbe.feature.trash.domain.service.TrashTypeFinder;
import com.trashheroesbe.feature.trash.dto.response.TrashDescriptionResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashItemResponse;
import com.trashheroesbe.feature.trash.dto.response.TrashTypeResponse;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.infrastructure.port.gpt.ChatAIClientPort;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final SearchLogService searchLogService;
    private final TrashTypeFinder trashTypeFinder;
    private final TrashItemFinder trashItemFinder;
    private final TrashDescriptionFinder trashDescriptionFinder;
    private final ChatAIClientPort chatAIClientPort;

    public List<TrashTypeResponse> getTrashTypes() {
        List<TrashType> trashTypes = trashTypeFinder.getAllTrashTypes();

        return trashTypes.stream()
            .filter(trashType -> trashType.getType() != Type.UNKNOWN)
            .map(TrashTypeResponse::from)
            .collect(Collectors.toList());
    }

    public List<TrashItemResponse> getTrashItems(Long trashTypeId) {
        List<TrashItem> trashItems = trashItemFinder.findTrashItemsByTrashTypeId(trashTypeId);
        return trashItems.stream()
            .map(TrashItemResponse::from)
            .collect(Collectors.toList());
    }

    public TrashDescriptionResponse getTrashDescriptions(Long trashTypeId) {
        TrashDescription trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
            trashTypeId);
        return TrashDescriptionResponse.from(trashDescription);
    }

    public TrashDescriptionResponse searchTrashDescription(String keyword, User user) {
        Type type = chatAIClientPort.findSimilarTrashItem(keyword);
        if (type == null) {
            return TrashDescriptionResponse.ofNotFound();
        }

        TrashType trashType = trashTypeFinder.getTrashType(type);
        TrashDescription trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
            trashType.getId());

        searchLogService.log(QUESTION, trashType, user);
        return TrashDescriptionResponse.from(trashDescription);
    }
}
