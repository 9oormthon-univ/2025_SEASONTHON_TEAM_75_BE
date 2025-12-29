package com.trashheroesbe.feature.trash.dto.response;

import com.trashheroesbe.feature.point.dto.response.PointEarnedResult;
import com.trashheroesbe.feature.trash.domain.entity.Trash;

public record TrashCreationResult(
    Trash saved,
    PointEarnedResult pointResult
) {

}
