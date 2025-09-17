package com.trashheroesbe.global.context;

import com.trashheroesbe.feature.user.dto.response.UserBadgeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BadgeContextHolder {

    private static final ThreadLocal<List<UserBadgeResponse>> NEW_BADGES = new ThreadLocal<>();


    public static void setNewBadges(List<UserBadgeResponse> badges) {
        NEW_BADGES.set(badges);
    }

    public static List<UserBadgeResponse> getNewBadges() {
        List<UserBadgeResponse> badges = NEW_BADGES.get();
        return badges != null ? badges : List.of();
    }

    public static void clear() {
        NEW_BADGES.remove();
    }
}