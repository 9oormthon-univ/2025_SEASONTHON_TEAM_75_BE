package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "쓰레기 품목")
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
