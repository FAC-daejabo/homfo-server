package com.homfo.sms.dto;

/**
 * 문자 인증을 위한 DTO 입니다.
 * */
public record SmsCodeTransactionDto(
        String phoneNumber,

        SmsCodeDto before,

        SmsCodeDto after
) {
}
