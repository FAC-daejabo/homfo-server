package com.hompo.user.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * 사용자의 계정 상태입니다.
 * */
public enum UserStatus {
    /**
     * 계정이 현재 사용 가능합니다.
     * */
    USE("U"),

    /**
     * 계정이 삭제된 상태입니다.
     * */
    DELETED("D"),

    /**
     * 계정이 정지된 상태입니다.
     * */
    STOPPED("S");

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }

    public boolean isDeleted() {
        return this == UserStatus.DELETED;
    }

    public boolean isStopped() {
        return this == UserStatus.STOPPED;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static UserStatus fromCode(String code) {
        return Arrays.stream(UserStatus.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("유저 상태에 %s가 존재하지 않습니다.", code)));
    }
}