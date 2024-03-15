package com.homfo.user.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * 직원 계정 상태입니다.
 * */
public enum EmployeeStatus {
    /**
     * 계정이 현재 사용 가능합니다.
     * */
    USE("U"),

    /**
     * 계정이 삭제된 상태입니다.
     * */
    DELETED("D"),

    /**
     * 회원가입을 요청한 상태입니다.
     * */
    PENDING("P"),

    /**
     * 계정이 정지된 상태입니다.
     * */
    STOPPED("S");

    private final String code;

    EmployeeStatus(String code) {
        this.code = code;
    }

    /**
     * 사용자가 삭제된 상태인지 확인합니다.
     * */
    public boolean isDeleted() {
        return this == EmployeeStatus.DELETED;
    }

    /**
     * 사용자가 정지된 상태인지 확인합니다.
     * */
    public boolean isStopped() {
        return this == EmployeeStatus.STOPPED;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static EmployeeStatus fromCode(String code) {
        return Arrays.stream(EmployeeStatus.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}