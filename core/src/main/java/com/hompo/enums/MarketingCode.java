package com.hompo.enums;

import java.util.Arrays;

public enum MarketingCode {
    sendInformationToThirdParty;

    public String getCode() {
        return name();
    }

    public static MarketingCode fromCode(String code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}
