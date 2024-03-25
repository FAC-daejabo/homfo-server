package com.homfo.error;

import lombok.Getter;

/**
 * 문자 관련한 에러 코드입니다.
 * */
@Getter
public enum CommonErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("SERVER_ERROR_00000001", "서버에서 에러가 발생했습니다."),
    DUPLICATE("SERVER_ERROR_00000002", "다시 시도해주세요."),
    BAD_REQUEST("SERVER_ERROR_00000003", "잘못된 요청입니다.");

    private final String code;

    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
