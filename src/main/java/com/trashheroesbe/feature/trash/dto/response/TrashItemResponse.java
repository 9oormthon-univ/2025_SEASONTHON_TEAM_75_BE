package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import lombok.Builder;

@Builder
public record TrashItemResponse(
    Long trashItemId,
    String itemName,
    String typeName
) {

    public static TrashItemResponse from(TrashItem trashItem) {
        return TrashItemResponse.builder()
            .trashItemId(trashItem.getId())
            .itemName(trashItem.getName())
            .typeName(trashItem.getTrashType().getType().getNameKo())
            .build();
    }
}
