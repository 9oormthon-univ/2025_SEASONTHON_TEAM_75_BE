package com.trashheroesbe.feature.district.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "districts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class District {

    @Id
    @Column(length = 12)
    private String id;

    @Column(nullable = false)
    private String sido;

    @Column
    private String sigungu;

    @Column
    private String eupmyeondong;

}
