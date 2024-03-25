package com.homfo.sms.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Sms 인증 코드 상태입니다.
 * */
public enum SmsCodeStatus {
    /**
     * 인증에 성공한 상태입니다.
     * */
    SUCCESS("S"),

    /**
     * 인증을 요청한 상태입니다.
     * */
    REQUESTED("R");

    private final String code;

    SmsCodeStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static SmsCodeStatus fromCode(String code) {
        return Arrays.stream(SmsCodeStatus.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}