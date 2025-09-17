package com.trashheroesbe.feature.trash.domain.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Schema(description = "쓰레기 카테고리")
public enum Type {

    // Recyclable (R)
    PAPER("R01", "Paper", "종이류"),
    PAPER_PACK("R02", "Paper Pack", "종이팩"),
    PLASTIC("R03", "Plastic", "플라스틱류"),
    PET("R04", "PET", "PET(투명 페트병)"),
    VINYL_FILM("R05", "Vinyl/Film", "비닐류"),
    STYROFOAM("R06", "Styrofoam (EPS)", "스티로폼류"),
    GLASS("R07", "Glass", "유리병류"),
    METAL("R08", "Metal", "캔류·고철류"),
    TEXTILES("R09", "Textiles", "의류·섬유류"),
    E_WASTE("R10", "E-Waste", "폐가전류"),
    HAZARDOUS_SMALL_WASTE("R11", "Hazardous Small Waste", "소형 유해 폐기물"),

    // Food Waste (F)
    FOOD_WASTE("F01", "Food Waste", "음식물 쓰레기"),

    // Non-Recyclable (N)
    NON_RECYCLABLE("N01", "Non-Recyclable Waste", "일반 쓰레기"),

    // Bulky Waste (B)
    BULKY_WASTE("B01","Bulky Waste", "대형폐기물"),

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