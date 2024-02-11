package com.hompo.enums;

import java.util.Arrays;

public enum MarketingCode {
    sendInformationToThirdParty("MARKETING_CODE_00000001");

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
