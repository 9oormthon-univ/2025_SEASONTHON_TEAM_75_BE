package com.trashheroesbe.feature.trash.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "trash_description")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trash_description_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_type_id", nullable = false)
    private TrashType trashType;

    @Lob
    @Column(name = "method_detail", nullable = false)
    private String methodDetail;

    @Lob
    @Column(name = "caution_note")
    private String cautionNote;

    // 줄바꿈으로 STEP 리스트 변환
    public java.util.List<String> steps() {
        if (methodDetail == null || methodDetail.isBlank()) return java.util.List.of();
        return java.util.Arrays.stream(methodDetail.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}