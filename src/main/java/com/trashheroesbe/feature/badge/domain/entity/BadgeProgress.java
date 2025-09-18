package com.trashheroesbe.feature.badge.domain.entity;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "badge_progress")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeProgress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column
    private Integer progressCount;

    @ElementCollection
    @CollectionTable(
        name = "badge_progress_trash_types",
        joinColumns = @JoinColumn(name = "badge_progress_id")
    )
    @Column(name = "trash_type", nullable = false, length = 64)
    private Set<String> trashTypes = new LinkedHashSet<>();


    public void incrementCount() {
        this.progressCount = (this.progressCount == null) ? 1 : this.progressCount + 1;
    }

    public void addTrashType(String trashType) {
        if (this.trashTypes == null) {
            this.trashTypes = new LinkedHashSet<>();
        }
        this.trashTypes.add(trashType);
    }

    public int getUniqueTrashTypeCount() {
        return this.trashTypes == null ? 0 : this.trashTypes.size();
    }
}
