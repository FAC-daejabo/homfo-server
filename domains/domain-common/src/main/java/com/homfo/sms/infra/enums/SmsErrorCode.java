package com.homfo.sms.infra.enums;

import com.homfo.error.ErrorCode;
import lombok.Getter;

/**
 * 문자 관련한 에러 코드입니다.
 * */
@Getter
public enum SmsErrorCode implements ErrorCode {
    LIMITED_SEND_SMS("SMS_ERROR_00000001", "인증 제한 횟수를 초과했습니다. 잠시 후 다시 시도해주세요.");

    private final String code;

    private final String message;

    SmsErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
