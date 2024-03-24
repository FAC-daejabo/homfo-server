package com.homfo.sms.dto;

public record SmsCodeDto(
        String phoneNumber,

        String code
) {
}
