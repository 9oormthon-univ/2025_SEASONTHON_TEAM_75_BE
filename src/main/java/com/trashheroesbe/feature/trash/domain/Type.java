package com.trashheroesbe.feature.trash.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Schema(description = "쓰레기 카테고리")
public enum Type {

    // Recyclable (R)
    PAPER("R01", "Paper", "종이류"),
    PLASTIC("R02", "Plastic", "플라스틱류"),
    VINYL_FILM("R03", "Vinyl/Film", "비닐류"),
    STYROFOAM("R04", "Styrofoam (EPS)", "스티로폼류"),
    GLASS("R05", "Glass", "유리병류"),
    METAL("R06", "Metal", "캔류·고철류"),
    TEXTILES("R07", "Textiles", "의류·섬유류"),
    E_WASTE("R08", "E-Waste", "폐가전류"),
    HAZARDOUS_SMALL_WASTE("R09", "Hazardous Small Waste", "기타(소형 유해 폐기물류)"),

    // Food Waste (F)
    FOOD_WASTE("F01", "Food Waste", "음식물 쓰레기"),

    // Non-Recyclable (N)
    CONTAMINATED_WASTE("N01", "Contaminated Waste", "오염된 폐기물"),
    CERAMICS("N02", "Ceramics", "도자기·사기류"),
    HYGIENE_PRODUCTS("N03", "Hygiene Products", "위생용품"),
    OTHER_NON_RECYCLABLES("N04", "Other Non-Recyclables", "기타 재활용 불가품목"),

    UNKNOWN("UNK", "Unknown", "미분류");

    private final String typeCode;
    private final String nameEn;
    private final String nameKo;

    Type(String typeCode, String nameEn, String nameKo) {
        this.typeCode = typeCode;
        this.nameEn = nameEn;
        this.nameKo = nameKo;
    }

    private static final Map<String, Type> BY_ID = new HashMap<>();
    private static final Map<String, Type> BY_EN = new HashMap<>();
    private static final Map<String, Type> BY_KO = new HashMap<>();

    static {
        for (Type t : values()) {
            BY_ID.put(t.typeCode, t);
            BY_EN.put(t.nameEn.toLowerCase(), t);
            BY_KO.put(t.nameKo, t);
        }
    }

    public static Type fromCode(String code) {
        if (code == null) {
            return UNKNOWN;
        }
        return BY_ID.getOrDefault(code.trim().toUpperCase(), UNKNOWN);
    }

    public static Type fromEn(String en) {
        if (en == null) {
            return UNKNOWN;
        }
        return BY_EN.getOrDefault(en.trim().toLowerCase(), UNKNOWN);
    }

    public static Type fromKo(String ko) {
        if (ko == null) {
            return UNKNOWN;
        }
        return BY_KO.getOrDefault(ko.trim(), UNKNOWN);
    }

    public static Type safeValueOf(String name) {
        if (name == null) {
            return UNKNOWN;
        }
        try {
            return Type.valueOf(name.trim().toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}