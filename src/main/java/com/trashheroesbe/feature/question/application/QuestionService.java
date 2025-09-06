package com.trashheroesbe.feature.question.application;

import static com.trashheroesbe.feature.search.domain.LogSource.QUESTION;
import static com.trashheroesbe.feature.trash.domain.type.ItemType.NORMAL;

import com.trashheroesbe.feature.search.application.SearchLogService;
import com.trashheroesbe.feature.trash.domain.type.Type;
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
import com.trashheroesbe.infrastructure.adapter.out.gpt.dto.SimilarResult;
import com.trashheroesbe.infrastructure.port.gpt.ChatAIClientPort;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final SearchLogService searchLogService;
    private final TrashTypeFinder trashTypeFinder;
    private final TrashItemFinder trashItemFinder;
    private final TrashDescriptionFinder trashDescriptionFinder;
    private final ChatAIClientPort chatAIClientPort;

    @Transactional(readOnly = true)
    public List<TrashTypeResponse> getTrashTypes() {
        List<TrashType> trashTypes = trashTypeFinder.getAllTrashTypes();

        return trashTypes.stream()
            .filter(trashType -> trashType.getType() != Type.UNKNOWN)
            .map(TrashTypeResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrashItemResponse> getTrashItems(Long trashTypeId) {
        List<TrashItem> trashItems = trashItemFinder.findTrashItemsByTrashTypeId(trashTypeId);
        return trashItems.stream()
            .map(TrashItemResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TrashDescriptionResponse getTrashDescriptions(Long trashTypeId, User user) {
        TrashDescription trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
            trashTypeId);
        searchLogService.log(QUESTION, trashDescription.getTrashType(), user);
        return TrashDescriptionResponse.from(trashDescription);
    }

    public TrashDescriptionResponse searchTrashDescription(String keyword, User user) {
        List<String> itemNames = trashItemFinder.getTrashItemNames();
        List<Type> types = trashTypeFinder.getAllTypes();

        SimilarResult result = chatAIClientPort.findSimilarTrashItem(keyword, itemNames, types);

        TrashDescription trashDescription;
        if (result.getItemName().isPresent()) {
            String itemName = result.getItemName().get();
            TrashItem trashItem = trashItemFinder.getTrashItemByName(itemName);

            if (trashItem.getItemType() == NORMAL) {
                trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
                    trashItem.getTrashType().getId());
                searchLogService.log(QUESTION, trashItem.getTrashType(), user);
                return TrashDescriptionResponse.from(trashDescription);
            } else {
                trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
                    trashItem.getRedirectTrashType().getId());
                searchLogService.log(QUESTION, trashItem.getRedirectTrashType(), user);
                return TrashDescriptionResponse.from(trashDescription);
            }

        } else if (result.getType().isPresent()) {
            Type type = result.getType().get();
            TrashType trashType = trashTypeFinder.getTrashType(type);
            trashDescription = trashDescriptionFinder.findTrashDescriptionsByTrashTypeId(
                trashType.getId());

            searchLogService.log(QUESTION, trashType, user);
            return TrashDescriptionResponse.from(trashDescription);
        } else {
            return TrashDescriptionResponse.ofNotFound();
        }
    }
}
