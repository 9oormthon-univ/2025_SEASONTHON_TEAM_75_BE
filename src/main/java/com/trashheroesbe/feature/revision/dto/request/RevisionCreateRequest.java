package com.trashheroesbe.feature.revision.dto.request;

import java.time.LocalDate;

public record RevisionCreateRequest(
    String subTitle,
    String title,
    String content,
    LocalDate revisionDate,
    Long trashTypeId
) {

}
