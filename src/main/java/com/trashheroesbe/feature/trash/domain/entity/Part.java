package com.trashheroesbe.feature.trash.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "part")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Part {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "trash_type_id")
    private TrashType trashType;

    @Column(nullable = false)
    private String name;
}