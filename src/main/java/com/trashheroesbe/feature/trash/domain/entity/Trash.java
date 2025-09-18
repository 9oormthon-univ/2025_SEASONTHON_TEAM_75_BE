package com.trashheroesbe.feature.trash.domain.entity;


import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "trash")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trash extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;
    
    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_type_id")
    private TrashType trashType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_item_id")
    private TrashItem trashItem;

    public void applyItem(TrashItem item) {
        this.trashItem = item;
    }

    @OneToMany(mappedBy = "trash", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TrashPart> trashParts = new ArrayList<>();

    public static Trash create(User user, String imageUrl, String name) {
        return Trash.builder()
                .imageUrl(imageUrl)
                .name(name)
                .user(user)
                .build();
    }

    public void applyAnalysis(TrashType type) {
        this.trashType = type;
    }
}