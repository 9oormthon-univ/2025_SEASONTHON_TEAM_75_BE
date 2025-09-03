package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.trash.domain.Type;
import com.trashheroesbe.feature.trash.domain.entity.TrashDescription;
import com.trashheroesbe.feature.trash.domain.entity.TrashItem;
import java.util.List;
import lombok.Builder;

@Builder
public record TrashDescriptionResponse(
    Long trashDescriptionId,
    List<String> guideSteps,
    String cautionNote,
    String typeName
) {

    public static TrashDescriptionResponse from(TrashDescription trashDescription) {
        List<String> steps = trashDescription.steps();
        return TrashDescriptionResponse.builder()
            .trashDescriptionId(trashDescription.getId())
            .guideSteps(steps)
            .cautionNote(trashDescription.getCautionNote())
            .typeName(trashDescription.getTrashType().getType().getNameKo())
            .build();
    }

    public static TrashDescriptionResponse ofNotFound() {
        return TrashDescriptionResponse.builder()
            .trashDescriptionId(-1L)
            .guideSteps(List.of())
            .cautionNote("해당하는 키워드에 분리배출 방법을 찾지 못했습니다.")
            .typeName(Type.UNKNOWN.getNameKo())
            .build();
    }
}
