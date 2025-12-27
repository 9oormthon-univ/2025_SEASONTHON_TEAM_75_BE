package com.trashheroesbe.feature.point.domain.entity;

import com.trashheroesbe.feature.point.domain.type.ActionType;
import com.trashheroesbe.feature.point.domain.type.PointReason;
import com.trashheroesbe.feature.user.domain.entity.User;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "user_point_history")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private PointReason pointReason;

    @Column(nullable = false)
    private Long relatedEntityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static UserPointHistory create(
        User user,
        Integer amount,
        Integer balanceAfter,
        ActionType actionType,
        PointReason pointReason,
        Long relatedEntityId
    ) {
        return UserPointHistory.builder()
            .user(user)
            .amount(amount)
            .balanceAfter(balanceAfter)
            .actionType(actionType)
            .pointReason(pointReason)
            .relatedEntityId(relatedEntityId)
            .build();
    }
}
