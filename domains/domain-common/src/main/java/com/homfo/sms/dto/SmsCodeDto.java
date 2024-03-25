package com.homfo.sms.dto;

import java.time.LocalDateTime;

/**
 * 문자 인증을 위한 DTO 입니다.
 * */
public record SmsCodeDto(
        String phoneNumber,

        String code,

        LocalDateTime createdAt
) {
}
