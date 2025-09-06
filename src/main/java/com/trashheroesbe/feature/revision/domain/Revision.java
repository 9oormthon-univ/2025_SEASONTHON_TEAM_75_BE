package com.trashheroesbe.feature.revision.domain;

import com.trashheroesbe.feature.revision.dto.request.RevisionCreateRequest;
import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "revisions")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Revision extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String subTitle;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private LocalDate revisionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_type_id", nullable = false)
    private TrashType trashType;

    public static Revision createRevision(RevisionCreateRequest request, TrashType trashType) {
        return Revision.builder()
            .subTitle(request.subTitle())
            .title(request.title())
            .content(request.content())
            .revisionDate(request.revisionDate())
            .trashType(trashType)
            .build();
    }
}
