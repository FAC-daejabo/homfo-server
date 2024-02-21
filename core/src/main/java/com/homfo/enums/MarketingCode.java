package com.homfo.enums;

import java.util.Arrays;

/**
 * 마케팅 코드를 관리합니다.
 * */
public enum MarketingCode {
    SEND_INFORMATION_TO_THIRD_PARTY("MARKETING_CODE_00000001");

    private final String code;

    MarketingCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MarketingCode fromCode(String code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}
