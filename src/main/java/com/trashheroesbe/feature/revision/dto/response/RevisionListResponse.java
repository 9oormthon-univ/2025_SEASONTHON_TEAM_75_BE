package com.trashheroesbe.feature.revision.dto.response;

import com.trashheroesbe.feature.revision.domain.Revision;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record RevisionListResponse(
    Long revisionId,
    String subTitle,
    String trashTypeName,
    LocalDate revisionDate
) {

    public static RevisionListResponse from(Revision revision) {
        return RevisionListResponse.builder()
            .revisionId(revision.getId())
            .subTitle(revision.getSubTitle())
            .trashTypeName(revision.getTrashType().getType().getNameKo())
            .revisionDate(revision.getRevisionDate())
            .build();
    }
}
