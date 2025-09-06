package com.trashheroesbe.feature.rank.domain;

import com.trashheroesbe.feature.trash.domain.entity.TrashType;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "trash_rank")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashRank extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long rankId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trash_type_id", nullable = false)
    private TrashType trashType;

    @Column(name = "rank_order", nullable = false, columnDefinition = "INT COMMENT '순위'")
    private Integer rankOrder;

    @Column(name = "previous_rank")
    private Integer previousRank;

    @Column(name = "search_count", nullable = false, columnDefinition = "INT")
    private Integer searchCount;

    @Column(name = "previous_search_count", columnDefinition = "INT")
    private Integer previousSearchCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "trend_direction", columnDefinition = "VARCHAR(10) COMMENT '트렌드 방향'")
    private TrendDirection trendDirection;

    @Column(name = "last_updated", columnDefinition = "DATETIME COMMENT '마지막 업데이트 시간'")
    private LocalDateTime lastUpdated;


    public void updateTotalSearchCount(Integer searchCount, Integer recentSearchCount) {
        this.previousSearchCount = searchCount;
        this.searchCount = searchCount + recentSearchCount;
    }

    public void calculateTrendDirection(Integer newRankOrder) {
        if (rankOrder == null) {
            this.trendDirection = TrendDirection.NEW;
        }

        if (newRankOrder < rankOrder) {
            this.trendDirection = TrendDirection.UP;
        } else if (newRankOrder > rankOrder) {
            this.trendDirection = TrendDirection.DOWN;
        } else {
            this.trendDirection = TrendDirection.SAME;
        }
    }

    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateRankOrder(Integer newRankOrder) {
        this.previousRank = this.rankOrder;
        this.rankOrder = newRankOrder;
    }

}
