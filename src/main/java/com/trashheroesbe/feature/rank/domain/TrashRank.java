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

    @Column(name = "week_start_date", nullable = false, columnDefinition = "DATE COMMENT '시작날짜'")
    private LocalDate weekStartDate;

    @Column(name = "week_end_date", nullable = false)
    private LocalDate weekEndDate;

    @Column(name = "rank_order", nullable = false, columnDefinition = "INT COMMENT '순위'")
    private Integer rankOrder;

    @Column(name = "previous_rank")
    private Integer previousRank;

    @Column(name = "search_count", nullable = false, columnDefinition = "INT COMMENT '주간 검색 수'")
    private Integer searchCount;

    @Column(name = "previous_search_count", columnDefinition = "INT COMMENT '이전 주간 검색 수'")
    private Integer previousSearchCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "trend_direction", columnDefinition = "VARCHAR(10) COMMENT '트렌드 방향'")
    private TrendDirection trendDirection;
}
