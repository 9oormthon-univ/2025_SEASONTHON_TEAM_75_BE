package com.trashheroesbe.feature.revision.dto.response;

import com.trashheroesbe.feature.revision.domain.Revision;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record RevisionResponse(
    Long revisionId,
    String subTitle,
    String title,
    String content,
    LocalDate revisionDate,
    String trashTypeName
) {

    public static RevisionResponse from(Revision revision) {
        return RevisionResponse.builder()
            .revisionId(revision.getId())
            .subTitle(revision.getSubTitle())
            .title(revision.getTitle())
            .content(revision.getContent())
            .revisionDate(revision.getRevisionDate())
            .revisionDate(revision.getRevisionDate())
            .trashTypeName(revision.getTrashType().getType().getNameKo())
            .build();
    }
}
