package com.trashheroesbe.feature.trash.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "trash_part",
        uniqueConstraints = @UniqueConstraint(columnNames = {"trash_id","part_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashPart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_id") private Trash trash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")  private Part part;

    public TrashPart(Trash trash, Part part) {
        this.trash = trash;
        this.part = part;
    }
}