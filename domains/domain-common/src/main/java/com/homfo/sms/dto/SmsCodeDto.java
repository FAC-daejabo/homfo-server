package com.homfo.sms.dto;

/**
 * 문자 인증을 위한 DTO 입니다.
 * */
public record SmsCodeDto(
        String phoneNumber,

        String code
) {
}
