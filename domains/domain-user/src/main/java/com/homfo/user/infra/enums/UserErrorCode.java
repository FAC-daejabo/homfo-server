package com.homfo.user.infra.enums;

import com.homfo.error.ErrorCode;
import lombok.Getter;

@Getter
public enum UserErrorCode implements ErrorCode {
    ALREADY_EXIST_USER("USER_ERROR_00000001", "이미 존재하는 사용자입니다."),
    NOT_EXIST_USER("USER_ERROR_00000002", "존재하지 않는 사용자입니다."),
    NOT_EXIST_TOKEN("USER_ERROR_00000003", "토큰이 존재하지 않습니다.");

    private final String code;

    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
