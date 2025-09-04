package com.trashheroesbe.feature.trash.domain.entity;

import com.trashheroesbe.feature.trash.domain.Type;
import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import lombok.*;

@Getter
@Entity
@Table(name = "trash_type", uniqueConstraints = {
    @UniqueConstraint(name = "uk_trash_type_type", columnNames = "trash_type")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trash_type", length = 50, nullable = false)
    private Type type;

    public static TrashType of(Type type) {
        return TrashType.builder()
            .type(type)
            .build();
    }
}