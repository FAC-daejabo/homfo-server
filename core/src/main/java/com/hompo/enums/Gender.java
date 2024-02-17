package com.hompo.enums;

import java.util.Arrays;

/**
 * 성별입니다.
 * */
public enum Gender {
    MAN("M"),

    WOMAN("W");
    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Gender fromCode(String code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}