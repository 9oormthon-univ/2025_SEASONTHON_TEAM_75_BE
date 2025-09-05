package com.trashheroesbe.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateTimeUtils {

    public static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static LocalDate getCurrentWeekStart() {
        LocalDate today = LocalDate.now();
        return today.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getCurrentWeekEnd() {
        LocalDate today = LocalDate.now();
        return today.with(DayOfWeek.SUNDAY);
    }

    public static LocalDate getWeekStart(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    public static LocalDate getWeekEnd(LocalDate date) {
        return date.with(DayOfWeek.SUNDAY);
    }

    public static String getTodayKorean() {
        return toKoreanDay(LocalDate.now(KST).getDayOfWeek());
    }

    public static LocalDate getTodayDate() {
        return LocalDate.now(KST);
    }

    public static String toKoreanDay(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "월요일";
            case TUESDAY -> "화요일";
            case WEDNESDAY -> "수요일";
            case THURSDAY -> "목요일";
            case FRIDAY -> "금요일";
            case SATURDAY -> "토요일";
            case SUNDAY -> "일요일";
        };
    }
}
