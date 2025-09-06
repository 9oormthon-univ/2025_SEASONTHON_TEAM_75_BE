package com.trashheroesbe.feature.revision.application;

import com.trashheroesbe.feature.revision.domain.Revision;
import com.trashheroesbe.feature.revision.dto.request.RevisionCreateRequest;
import com.trashheroesbe.feature.revision.infrastructure.RevisionRepository;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.feature.trash.domain.service.TrashTypeFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RevisionAdminService {

    private final TrashTypeFinder trashTypeFinder;
    private final RevisionRepository revisionRepository;

    public void createRevision(RevisionCreateRequest request) {
        TrashType trashType = trashTypeFinder.getTrashTypeById(request.trashTypeId());
        Revision revision = Revision.createRevision(request, trashType);
        revisionRepository.save(revision);
    }
}
