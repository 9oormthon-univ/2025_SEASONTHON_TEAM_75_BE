package com.trashheroesbe.feature.revision.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;

import com.trashheroesbe.feature.revision.domain.Revision;
import com.trashheroesbe.feature.revision.dto.response.RevisionListResponse;
import com.trashheroesbe.feature.revision.dto.response.RevisionResponse;
import com.trashheroesbe.feature.revision.infrastructure.RevisionRepository;
import com.trashheroesbe.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RevisionService {

    private final RevisionRepository revisionRepository;

    public List<RevisionListResponse> getRevisionList() {
        List<Revision> revisions = revisionRepository.findAllByOrderByRevisionDateAsc();
        return revisions.stream()
            .map(RevisionListResponse::from)
            .toList();
    }

    public RevisionResponse getRevisionById(Long revisionId) {
        Revision revision = revisionRepository.findById(revisionId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        return RevisionResponse.from(revision);
    }
}
