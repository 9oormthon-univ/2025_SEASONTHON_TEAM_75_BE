package com.trashheroesbe.feature.point.domain.entity;

import static com.trashheroesbe.global.response.type.ErrorCode.POINT_AMOUNT_MUST_BE_POSITIVE;

import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.global.entity.BaseTimeEntity;
import com.trashheroesbe.global.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "user_point",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_user_point_user_id",
        columnNames = "user_id"
    )
)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Version
    @Column(nullable = false)
    private Long version;

    public void earnPoints(Integer points) {
        if (points <= 0) {
            throw new BusinessException(POINT_AMOUNT_MUST_BE_POSITIVE);
        }
        this.totalPoint += points;
    }

    public static UserPoint createInit(User user) {
        return UserPoint.builder()
            .totalPoint(0)
            .user(user)
            .version(0L)
            .build();
    }
}
