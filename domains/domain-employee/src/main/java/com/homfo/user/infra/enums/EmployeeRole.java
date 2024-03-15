package com.homfo.user.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * 직원 권한입니다.
 * */
public enum EmployeeRole {
    /**
     * 직원 권한입니다.
     * */
    EMPLOYEE("직원"),

    /**
     * 관리자 권한입니다.
     * */
    ADMIN("관리자");

    private final String code;

    EmployeeRole(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static EmployeeRole fromCode(String code) {
        return Arrays.stream(EmployeeRole.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}