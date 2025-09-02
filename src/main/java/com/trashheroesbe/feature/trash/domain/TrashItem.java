package com.trashheroesbe.feature.trash.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "trash_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_trash_item_type_name",
                columnNames = {"trash_type_id","name"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_type_id", nullable = false)
    private TrashType trashType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public static TrashItem of(TrashType type, String name) {
        return TrashItem.builder()
                .trashType(type)
                .name(name)
                .build();
    }
}