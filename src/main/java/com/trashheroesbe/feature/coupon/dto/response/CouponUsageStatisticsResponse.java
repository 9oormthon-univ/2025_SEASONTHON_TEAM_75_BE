package com.trashheroesbe.feature.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "쿠폰 사용 통계 응답")
public record CouponUsageStatisticsResponse(

    @Schema(description = "전체 사용 통계")
    UsageSummary total,

    @Schema(description = "주간 사용 통계")
    UsageSummary weekly,

    @Schema(description = "월간 사용 통계")
    UsageSummary monthly
) {

    @Schema(description = "사용 통계 요약")
    public record UsageSummary(

        @Schema(description = "사용된 쿠폰 수", example = "150")
        long count,

        @Schema(description = "집계 기간")
        PeriodRange period
    ) {

        @Schema(description = "집계 기간 범위")
        public record PeriodRange(

            @Schema(
                description = "집계 시작 일시",
                type = "string",
                format = "date-time",
                example = "2026-03-09T00:00:00"
            )
            LocalDateTime from,

            @Schema(
                description = "집계 종료 일시",
                type = "string",
                format = "date-time",
                example = "2026-03-14T15:30:00"
            )
            LocalDateTime to
        ) {

        }
    }
}
