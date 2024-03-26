package com.homfo.sms.infra.enums;

import com.homfo.error.ErrorCode;
import lombok.Getter;

/**
 * 문자 관련한 에러 코드입니다.
 * */
@Getter
public enum SmsErrorCode implements ErrorCode {
    NOT_VALID_SMS("SMS_ERROR_00000001", "올바르지 않은 정보입니다."),
    LIMITED_SEND_SMS("SMS_ERROR_00000002", "인증 제한 횟수를 초과했습니다. 잠시 후 다시 시도해주세요."),
    DUPLICATE_REQUEST_SMS("SMS_ERROR_00000003", "잠시 후 다시 요청해주세요."),
    FAILED_SEND_SMS("SMS_ERROR_00000004", "메세지 전송에 실패했습니다."),
    NOT_EXIST_SMS("SMS_ERROR_00000005", "정보를 찾지 못 했습니다.");

    private final String code;

    private final String message;

    SmsErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
